"""
02_expand_sku_from_category.py  (time-varying shares + lifecycle)
- 입력: cleaned_domain_sales.csv (없으면 domain_sales.csv), sku_catalog.csv
- 핵심: 주별 점유율을 base_share 주변 랜덤워크로 생성 + 출시/램프업/감쇠 + 드문 프로모션 충격
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
NOISE_SCALE = 0.03           # 주별 미세 잡음
RHO = 0.9                    # 점유율 랜덤워크 모멘텀(0.8~0.95)
RW_SIGMA = 0.10              # 랜덤워크 분산(로그공간)
RAMP_WEEKS = 10              # 출시 후 램프업
DECAY_HALF_LIFE = 90         # 절반감쇠 주수
PROMO_RATE = 0.05            # 주별 프로모션 발생 확률
PROMO_STRENGTH = 0.25        # 프로모션 승수
DEFAULT_PROMO_BOOST = 0.20
ID_PATTERN = r"^[A-Z0-9\-]+$"
MISC_KEYS = {"", "기타", "기타가전", "기타가전류", "기타제품", "etc", "misc", "others", "other"}

def _normalize(x):
    s = x.sum()
    return x / s if s > 0 else x

def _norm_cat(x: str) -> str:
    if pd.isna(x): return ""
    x = str(x).strip()
    x = re.sub(r"\s+", "", x)
    x = x.replace("_","").replace("-","")
    return x.lower()

def _load_domain():
    dtypes = {
        "warehouse_id": "Int64","store_id": "Int64",
        "product_id": "Int64","product_name": "string",
        "cat_top": "string","cat_mid": "string","cat_low": "string",
        "region": "string",
    }
    path = DOMAIN_CLEAN if DOMAIN_CLEAN.exists() else DOMAIN_RAW
    df = pd.read_csv(path, parse_dates=["target_date"], dtype=dtypes, low_memory=False)
    print(f"📦 Loaded {path.name}: {len(df):,} rows")
    return df

def _load_catalog():
    sku = pd.read_csv(SKU_CAT, dtype=str, low_memory=False)
    sku.columns = (sku.columns.astype(str).str.replace("\ufeff","",regex=False).str.strip())
    for col in ["sku_id","sku_name_en","sku_name_ko","cat_low","brand","series","model_code",
                "size_inch","volume_l","capacity_text","energy_grade","price_tier",
                "msrp_krw","launch_date","warranty_months","case_pack","min_order_qty","eol_flag","base_share"]:
        if col not in sku.columns:
            sku[col] = pd.NA
    # numeric/date cast
    for col in ["msrp_krw","warranty_months","case_pack","min_order_qty","eol_flag"]:
        sku[col] = pd.to_numeric(sku[col], errors="coerce")
    sku["launch_date"] = pd.to_datetime(sku["launch_date"], errors="coerce")
    sku["base_share"]  = pd.to_numeric(sku["base_share"], errors="coerce").fillna(0.0)
    for col in ["sku_id","sku_name_en","sku_name_ko","cat_low","brand","series","model_code",
                "size_inch","volume_l","capacity_text","energy_grade","price_tier"]:
        sku[col] = sku[col].astype("string").str.strip()
    return sku

def _load_promo():
    if PROMO.exists():
        p = pd.read_csv(PROMO, parse_dates=["target_date"])
        if "boost" not in p.columns: p["boost"] = DEFAULT_PROMO_BOOST
        return p
    return pd.DataFrame(columns=["sku_id","target_date","boost"])

def _validate_sku_ids(sku):
    bad = sku[~sku["sku_id"].astype(str).str.match(ID_PATTERN, na=False)]
    if len(bad) > 0:
        raise ValueError(f"sku_id 형식 오류: 예시 {bad.head(5)['sku_id'].tolist()} ... 전체 {len(bad)}건")

def _weeks_between(a, b):
    return int((b - b.normalize() if isinstance(b, pd.Timestamp) else b - a).days // 7) if pd.notna(a) and pd.notna(b) else 0

def _life_multiplier(launch_date, t):
    """출시 전 0, 이후 RAMP로 상승, 장기적으로 지수감쇠"""
    if pd.isna(launch_date):
        return 1.0
    weeks = int((t - launch_date).days // 7)
    if weeks < 0:
        return 0.0
    ramp = min(1.0, weeks / max(1, RAMP_WEEKS))
    lam = np.log(2) / max(1, DECAY_HALF_LIFE)
    decay = np.exp(-lam * max(0, weeks - RAMP_WEEKS))
    return ramp * decay + (1 - ramp) * 0.1

def _time_varying_shares(one_cat_week_df, sku_meta, rng):
    """카테고리 주간합 → SKU별 시간 가변 점유율로 분배"""
    g = one_cat_week_df.sort_values("target_date").copy()
    sku_rows = sku_meta.reset_index(drop=True)
    n = len(sku_rows)

    base = sku_rows["base_share"].to_numpy().clip(1e-6, 1.0)
    logits = np.log(base)                      # softmax 초기값
    promos = rng.random((len(g), n)) < PROMO_RATE

    rows = []
    for t_idx, dt in enumerate(g["target_date"]):
        # 랜덤워크(모멘텀+가우시안)
        logits = RHO*logits + (1-RHO)*np.log(base) + rng.normal(0, RW_SIGMA, size=n)
        s = np.exp(logits - np.max(logits)); s /= s.sum()

        # 미세 잡음 + 프로모션
        s = s * (1 + (rng.random(n)-0.5)*2*NOISE_SCALE)
        if promos[t_idx].any():
            s = s * (1 + promos[t_idx]*PROMO_STRENGTH)

        # 출시/수명 곱 → 재정규화
        life = np.array([_life_multiplier(sku_rows.loc[i,"launch_date"], dt) for i in range(n)])
        s = s * life
        if s.sum() <= 0:
            s = base.copy()
        s /= s.sum()

        # 행 누적
        rows.append(pd.DataFrame({
            "target_date": dt,
            "sku_id": sku_rows["sku_id"].tolist(),
            "share_norm": s
        }))
    rep = pd.concat(rows, ignore_index=True)
    rep = rep.merge(g[["target_date","actual_order_qty"]], on="target_date", how="left")
    rep["sku_qty"] = (rep["actual_order_qty"] * rep["share_norm"]).round(0).astype(int)
    return rep

def main():
    df  = _load_domain()
    sku = _load_catalog()
    _validate_sku_ids(sku)

    # 1) 카테고리 표준화 + 기타 제거
    sku["cat_low_norm"] = sku["cat_low"].apply(_norm_cat)
    df["cat_low_norm"]  = df["cat_low"].apply(_norm_cat)
    allowed = set(sku["cat_low_norm"].dropna().tolist())
    keep = df["cat_low_norm"].isin(allowed) & (~df["cat_low_norm"].isin(MISC_KEYS))
    if (len(df) - keep.sum())>0:
        print(f"🧹 Dropped by whitelist: {len(df)-keep.sum():,}/{len(df):,}")
    df = df[keep].copy()

    # 2) base_share 정규화(그룹합=1, 합=0이면 균등)
    pos_sum = sku.groupby("cat_low_norm")["base_share"].transform("sum")
    mask_pos = pos_sum > 1e-12
    sku.loc[mask_pos, "base_share"] = sku.loc[mask_pos, "base_share"] / pos_sum[mask_pos]
    zero_keys = sku.loc[~mask_pos, "cat_low_norm"].dropna().unique().tolist()
    for key in zero_keys:
        idx = sku.index[sku["cat_low_norm"]==key]
        n = len(idx)
        if n>0: sku.loc[idx,"base_share"] = 1.0/n
    if zero_keys:
        print(f"base_share 합 0 → 균등분배: {sorted(zero_keys)}")

    # 3) 카테고리-주간 합
    keys = ["warehouse_id","region","store_id","cat_low_norm","target_date"]
    cat_week = df.groupby(keys, as_index=False)["actual_order_qty"].sum()

    # 4) 카탈로그 조인
    cat_map = cat_week.merge(
        sku.rename(columns={"cat_low_norm":"cat_low_norm_join"}),
        left_on="cat_low_norm", right_on="cat_low_norm_join",
        how="left", validate="many_to_many"
    )
    if cat_map["sku_id"].isna().any():
        missing = cat_map.loc[cat_map["sku_id"].isna(),"cat_low_norm"].unique().tolist()
        raise ValueError(f"카탈로그 누락 cat_low_norm: {missing}")

    # 5) 그룹별 시간가변 점유율 생성
    rng = np.random.default_rng(42)
    out_list = []
    for (wh, region, store, cat), g_cat in cat_map.groupby(["warehouse_id","region","store_id","cat_low_norm"]):
        sku_meta = (g_cat[["sku_id","base_share","launch_date","sku_name_en","sku_name_ko","brand",
                           "series","model_code","size_inch","volume_l","capacity_text","energy_grade","price_tier",
                           "msrp_krw","warranty_months","case_pack","min_order_qty","eol_flag"]]
                    .drop_duplicates("sku_id"))
        tv = _time_varying_shares(
            one_cat_week_df = g_cat[["target_date","actual_order_qty"]].drop_duplicates(),
            sku_meta = sku_meta, rng = rng
        )
        tv["warehouse_id"] = wh
        tv["region"] = region
        tv["store_id"] = store
        tv["cat_low"] = cat
        tv = tv.merge(sku_meta, on="sku_id", how="left")
        out_list.append(tv)

    out = pd.concat(out_list, ignore_index=True)
    out = out[[
        "warehouse_id","region","store_id","target_date","cat_low",
        "sku_id","sku_name_en","sku_name_ko","brand","series","model_code",
        "size_inch","volume_l","capacity_text","energy_grade","price_tier",
        "msrp_krw","launch_date","warranty_months","case_pack","min_order_qty","eol_flag",
        "actual_order_qty","share_norm","sku_qty"
    ]].sort_values(["store_id","target_date","cat_low","sku_id"]).reset_index(drop=True)

    OUT_FULL.parent.mkdir(parents=True, exist_ok=True)
    out.to_csv(OUT_FULL, index=False)
    out.sample(min(2000,len(out)), random_state=42).to_csv(OUT_SAMPLE, index=False)
    print(f"saved:\n - {OUT_FULL}\n - {OUT_SAMPLE}\nrows={len(out):,}")

if __name__ == "__main__":
    main()
