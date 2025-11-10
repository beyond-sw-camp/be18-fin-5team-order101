"""
02b_expand_sku_from_category.py
- 입력: cleaned_domain_sales.csv (없으면 domain_sales.csv), sku_catalog.csv, (선택) promotions.csv
- 처리:
  * 카테고리 표준화 + 화이트리스트 필터 (이중 방어)
  * base_share × (1+noise) × (1+promo) 후 그룹 정규화로 SKU 분배
  * sku_id 형식 검증(영문 대문자/숫자/대시)
- 출력: domain_sales_sku.csv, sample_domain_sales_sku.csv
"""
from pathlib import Path
import pandas as pd
import numpy as np
import re

BASE = Path(__file__).resolve().parent
DOMAIN_CLEAN = BASE / "cleaned_domain_sales.csv"
DOMAIN_RAW   = BASE / "domain_sales.csv"
SKU_CAT      = BASE / "sku_catalog.csv"
PROMO        = BASE / "promotions.csv"   # optional: sku_id,target_date,boost

OUT_FULL   = BASE / "domain_sales_sku.csv"
OUT_SAMPLE = BASE / "sample_domain_sales_sku.csv"

# ===== Settings =====
NOISE_SCALE = 0.05          # ±5% random noise
DEFAULT_PROMO_BOOST = 0.20  # when promotions.csv has no 'boost'
ID_PATTERN = r"^[A-Z0-9\-]+$"  # uppercase letters / digits / dash only
MISC_KEYS = {"", "기타", "기타가전", "기타가전류", "기타제품", "etc", "misc", "others", "other"}

def _normalize(x):
    s = x.sum()
    return x / s if s > 0 else x

def _norm_cat(x: str) -> str:
    if pd.isna(x):
        return ""
    x = str(x).strip()
    x = re.sub(r"\s+", "", x)        # 모든 공백 제거
    x = x.replace("_", "").replace("-", "")
    return x.lower()

def _load_domain():
    domain_dtypes = {
        "warehouse_id": "Int64",
        "store_id": "Int64",
        "product_id": "Int64",
        "product_name": "string",
        "cat_top": "string",
        "cat_mid": "string",
        "cat_low": "string",
        "region": "string",
    }
    path = DOMAIN_CLEAN if DOMAIN_CLEAN.exists() else DOMAIN_RAW
    df = pd.read_csv(path, parse_dates=["target_date"], dtype=domain_dtypes, low_memory=False)
    print(f"📦 Loaded {path.name}: {len(df):,} rows")
    return df

def _load_catalog():
    # 1) 전체를 일단 문자열로 안전하게 읽는다 (열 밀림/따옴표 이슈 방지)
    sku = pd.read_csv(SKU_CAT, dtype=str, low_memory=False)

    # 2) 헤더/공백 표준화 (예상치 못한 공백/대소문자/숨은 BOM 방지)
    sku.columns = (
        sku.columns
          .astype(str)
          .str.replace("\ufeff", "", regex=False)  # BOM 제거
          .str.strip()
    )

    # 3) 필요한 컬럼이 없다면 생성해두기 (NaN 방지)
    for col in [
        "sku_id","sku_name_en","sku_name_ko","cat_low","brand","series","model_code",
        "size_inch","volume_l","capacity_text","energy_grade","price_tier",
        "msrp_krw","launch_date","warranty_months","case_pack","min_order_qty","eol_flag",
        "base_share"
    ]:
        if col not in sku.columns:
            sku[col] = pd.NA

    # 4) 숫자/날짜형만 개별 캐스팅 (에러는 NaN으로 흘려보낸다)
    for col in ["msrp_krw","warranty_months","case_pack","min_order_qty","eol_flag"]:
        sku[col] = pd.to_numeric(sku[col], errors="coerce")

    sku["launch_date"] = pd.to_datetime(sku["launch_date"], errors="coerce")
    sku["base_share"]  = pd.to_numeric(sku["base_share"], errors="coerce")

    # 5) 문자열 컬럼은 양쪽 공백 정리 (조인 안정성 ↑)
    for col in ["sku_id","sku_name_en","sku_name_ko","cat_low","brand","series","model_code",
                "size_inch","volume_l","capacity_text","energy_grade","price_tier"]:
        sku[col] = sku[col].astype("string").str.strip()

    return sku


def _load_promo():
    if PROMO.exists():
        p = pd.read_csv(PROMO, parse_dates=["target_date"])
        if "boost" not in p.columns:
            p["boost"] = DEFAULT_PROMO_BOOST
        return p
    return pd.DataFrame(columns=["sku_id", "target_date", "boost"])

def _validate_sku_ids(sku: pd.DataFrame):
    bad = sku[~sku["sku_id"].astype(str).str.match(ID_PATTERN, na=False)]
    if len(bad) > 0:
        ex = bad.head(5)["sku_id"].tolist()
        raise ValueError(f"sku_id 형식 오류(영문 대문자/숫자/대시만 허용): 예시 {ex} ... 전체 {len(bad)}건")

def main():
    # 0) load
    df  = _load_domain()
    sku = _load_catalog()

    # 1) 표준화/화이트리스트
    sku["cat_low_norm"] = sku["cat_low"].apply(_norm_cat)
    df["cat_low_norm"]  = df["cat_low"].apply(_norm_cat)

    allowed_norm = set(sku["cat_low_norm"].dropna().tolist())
    keep = df["cat_low_norm"].isin(allowed_norm) & (~df["cat_low_norm"].isin(MISC_KEYS))
    dropped = len(df) - keep.sum()
    if dropped:
        print(f"Dropped rows by whitelist in 02b: {dropped:,} / {len(df):,}")
    df = df[keep].copy()

    # 2) base_share 합=1 정규화
    # 2) base_share 합=1 정규화 (+합=0이면 균등분배)
    sku["base_share"] = pd.to_numeric(sku["base_share"], errors="coerce").fillna(0)

    # 합>0인 그룹: 1로 정규화
    pos_sum = sku.groupby("cat_low_norm")["base_share"].transform("sum")
    mask_pos = pos_sum > 1e-12
    sku.loc[mask_pos, "base_share"] = sku.loc[mask_pos, "base_share"] / pos_sum[mask_pos]

    # 합==0인 그룹: 균등 분배
    zero_keys = set(sku.loc[~mask_pos, "cat_low_norm"].unique()) - {None, pd.NA, ""}
    if zero_keys:
        for key in zero_keys:
            idx = sku.index[sku["cat_low_norm"] == key]
            n = len(idx)
            if n > 0:
                sku.loc[idx, "base_share"] = 1.0 / n
        print(f"base_share 합 0 → 균등 분배 적용: {sorted(list(zero_keys))}")


    # 3) 다국어 이름 보정
    if "sku_name_en" not in sku.columns:
        sku["sku_name_en"] = sku.get("sku_name", sku.get("sku_id"))
    if "sku_name_ko" not in sku.columns:
        sku["sku_name_ko"] = ""

    # 4) ID 형식 검증
    _validate_sku_ids(sku)

    # 5) 카테고리-주간 수요 집계
    keys = ["warehouse_id", "region", "store_id", "cat_low_norm", "target_date"]
    cat_week = df.groupby(keys, as_index=False)["actual_order_qty"].sum()

    # 6) 카탈로그 조인 (cat_low_norm 기준)
    cat_map = cat_week.merge(
        sku.rename(columns={"cat_low_norm": "cat_low_norm_join"}),
        left_on="cat_low_norm", right_on="cat_low_norm_join",
        how="left", validate="many_to_many"
    )
    if cat_map["sku_id"].isna().any():
        missing = cat_map.loc[cat_map["sku_id"].isna(), "cat_low_norm"].unique().tolist()
        raise ValueError(f"sku_catalog에 없는 cat_low가 존재합니다(정규화 기준): {missing}")

    # 7) share = base_share × (1+noise) × (1+promo)
    np.random.seed(42)
    noise = (np.random.rand(len(cat_map)) - 0.5) * 2 * NOISE_SCALE
    share = cat_map["base_share"] * (1 + noise)

    promo = _load_promo()
    if not promo.empty:
        cat_map = cat_map.merge(promo, on=["sku_id", "target_date"], how="left")
        cat_map["boost"] = cat_map["boost"].fillna(0.0)
        share = share * (1 + cat_map["boost"])

    cat_map["share_adj"] = share
    cat_map["share_norm"] = cat_map.groupby(keys)["share_adj"].transform(_normalize).fillna(0)

    # 8) 분배
    cat_map["sku_qty"] = (cat_map["actual_order_qty"] * cat_map["share_norm"]).round(0).astype(int)

    # 9) 출력
    out_cols = [
        "warehouse_id","region","store_id","target_date",
        # 표기용 카테고리(표준화된 값 사용)
        "cat_low_norm",
        "sku_id","sku_name_en","sku_name_ko","brand","series","model_code",
        "size_inch","volume_l","capacity_text","energy_grade","price_tier",
        "msrp_krw","launch_date","warranty_months","case_pack","min_order_qty","eol_flag",
        "actual_order_qty","share_norm","sku_qty"
    ]
    out = cat_map[out_cols].rename(columns={"cat_low_norm": "cat_low"}) \
                           .sort_values(["store_id","target_date","cat_low","sku_id"]) \
                           .reset_index(drop=True)
    
    cat_label_map = (
    sku.dropna(subset=["cat_low", "cat_low_norm"])
       .drop_duplicates(subset=["cat_low_norm"])
       .set_index("cat_low_norm")["cat_low"]
       .to_dict()
    )
    out["cat_low"] = out["cat_low"].map(cat_label_map).fillna(out["cat_low"])

    OUT_FULL.parent.mkdir(parents=True, exist_ok=True)
    out.to_csv(OUT_FULL, index=False)
    out.sample(min(2000, len(out)), random_state=42).to_csv(OUT_SAMPLE, index=False)

    print(f"saved:\n - {OUT_FULL}\n - {OUT_SAMPLE}\nrows={len(out):,}")

if __name__ == "__main__":
    main()
