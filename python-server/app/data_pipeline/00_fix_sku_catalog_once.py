from pathlib import Path
import pandas as pd
import re

BASE = Path(__file__).resolve().parent
CAT  = BASE / "sku_catalog.csv"

def norm(x: str) -> str:
    if pd.isna(x): return ""
    x = str(x).strip()
    x = re.sub(r"\s+", "", x)
    x = x.replace("_","").replace("-","")
    return x.lower()

def main():
    sku = pd.read_csv(CAT, dtype=str, low_memory=False)

    for col in ["cat_low", "base_share"]:
        if col not in sku.columns:
            sku[col] = pd.NA

    # 표기 오탈자/띄어쓰기 교정
    FIX = {
        "스마트워 치": "스마트워치",
        "모니 터": "모니터",
        "프 린터": "프린터",
    }
    sku["cat_low"] = sku["cat_low"].astype(str).replace(FIX)

    # 정규화용 키
    sku["cat_low_norm"] = sku["cat_low"].apply(norm)
    sku["base_share"] = pd.to_numeric(sku["base_share"], errors="coerce").fillna(0.0)

    # 그룹 합 계산
    grp = sku.groupby("cat_low_norm")["base_share"].sum()

    # 1) 합>0 → 합=1로 정규화
    mask_pos = sku["cat_low_norm"].isin(grp[grp>0].index)
    sums = sku.loc[mask_pos].groupby("cat_low_norm")["base_share"].transform("sum")
    sku.loc[mask_pos, "base_share"] = sku.loc[mask_pos, "base_share"] / sums

    # 2) 합==0 → 균등 분배(1/n)
    zero_keys = set(grp[grp==0].index)
    if zero_keys:
        for key in zero_keys:
            idx = sku.index[sku["cat_low_norm"]==key]
            n = len(idx)
            if n > 0:
                sku.loc[idx, "base_share"] = 1.0 / n

    # 출력(표기용 cat_low 유지)
    sku.drop(columns=["cat_low_norm"]).to_csv(CAT, index=False)
    print("sku_catalog.csv normalized (sum=1) with equal-split for zero-sum groups")

if __name__ == "__main__":
    main()
