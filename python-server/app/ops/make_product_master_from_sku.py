"""
make_product_master_from_sku.py
- sku_catalog.csv → product_master_load.csv 변환
- 목적: SKU 카탈로그를 product 테이블용 마스터로 추출
"""

from pathlib import Path
import pandas as pd
from pathlib import Path
import sys

BASE = Path(__file__).resolve().parent.parent / "data_pipeline"
SKU  = BASE / "sku_catalog.csv"
if not SKU.exists():
    print(f"Not found: {SKU}")
    print(f"cwd={Path.cwd()}")
    sys.exit(1)
BASE = Path(__file__).resolve().parent.parent / "data_pipeline"

SKU  = BASE / "sku_catalog.csv"
OUT  = BASE / "product_master_load.csv"

df = pd.read_csv(SKU, dtype=str, low_memory=False)

# 필수 컬럼 보정
if "sku_id" not in df.columns:
    raise ValueError("sku_catalog.csv에 sku_id 컬럼이 없습니다!")

df = df.rename(columns={
    "sku_id": "product_code",
    "sku_name_ko": "product_name"
})

need = ["product_code", "product_name"]
for c in need:
    if c not in df.columns:
        df[c] = ""

out = (
    df[need]
    .dropna(subset=["product_code"])
    .drop_duplicates("product_code")
    .sort_values("product_code")
    .reset_index(drop=True)
)

out.to_csv(OUT, index=False, encoding="utf-8-sig")
print(f"saved: {OUT} ({len(out)} rows)")
