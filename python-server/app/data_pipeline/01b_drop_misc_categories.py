"""
01b_drop_misc_categories.py
- 입력 : domain_sales.csv
- 처리 : '기타가전' 포함 각종 기타/미정 카테고리 행 전부 제거
- 출력 : cleaned_domain_sales.csv (다음 단계에서 이 파일을 우선 사용)
"""
from pathlib import Path
import pandas as pd
import re

BASE = Path(__file__).resolve().parent
SRC  = BASE / "domain_sales.csv"
OUT  = BASE / "cleaned_domain_sales.csv"
LOG  = BASE / "unmapped_summary.csv"

MISC_KEYS = {"", "기타", "기타가전", "기타가전류", "기타제품", "etc", "misc", "others", "other"}

def norm(x: str) -> str:
    if pd.isna(x): return ""
    s = str(x).strip()
    s = re.sub(r"\s+", "", s)
    s = s.replace("_","").replace("-","")
    return s.lower()

def main():
    if not SRC.exists():
        raise FileNotFoundError(SRC)
    df = pd.read_csv(SRC, low_memory=False)

    # cat_low 없으면 생성(빈값)
    if "cat_low" not in df.columns:
        df["cat_low"] = ""

    # 드롭 대상 플래그
    cat_raw = df["cat_low"].astype(str)
    cat_norm = cat_raw.apply(norm)
    drop_flag = cat_norm.isin(MISC_KEYS) | cat_norm.eq("nan") | cat_norm.eq("none") | cat_norm.eq("null")

    # 로그용 집계
    dropped = df[drop_flag].copy()
    kept    = df[~drop_flag].copy()

    if len(dropped) > 0:
        # 어떤 카테고리가 많이 걸렸는지
        agg = (dropped.assign(cat_low_norm=cat_norm[drop_flag])
                        .groupby(["cat_low","cat_low_norm"], dropna=False)["cat_low"]
                        .count()
                        .rename("rows")
                        .reset_index()
                        .sort_values("rows", ascending=False))
        agg.to_csv(LOG, index=False)

    kept.to_csv(OUT, index=False)

    print(f"saved: {OUT} ({len(kept):,} rows)")
    print(f"dropped (misc-like): {len(dropped):,} / {len(df):,}")
    if len(dropped) > 0:
        print(f"log: {LOG}")

if __name__ == "__main__":
    main()
