"""
03_make_features.py
- 입력 : domain_sales_sku.csv
- 출력 : features_all.csv, features_train.csv, features_test.csv
- 내용 : 주차 단위 라그/이동평균/시즌성/프로모션 피처 생성 + 4년/1년 split
"""
from pathlib import Path
import pandas as pd
import numpy as np

BASE = Path(__file__).resolve().parent
DATA = BASE / "domain_sales_sku.csv"
OUT_ALL  = BASE / "features_all.csv"
OUT_TR   = BASE / "features_train.csv"
OUT_TE   = BASE / "features_test.csv"

# 설정
FORECAST_FREQ = "W-MON"    # 주차 기준(월요일 주간)
LAGS = [1, 2, 4, 8, 12]    # 주 단위 라그
MAS  = [4, 8, 12]          # 이동평균(주)
TEST_WEEKS = 52            # 1년 테스트
MIN_HISTORY_WEEKS = 16     # 최소 4개월 치 데이터 이상인 시계열만 사용

def add_time_features(df: pd.DataFrame) -> pd.DataFrame:
    dt = pd.to_datetime(df["target_date"])
    df["year"] = dt.dt.year
    df["weekofyear"] = dt.dt.isocalendar().week.astype(int)
    df["month"] = dt.dt.month
    # 계절성: 주기적 특성
    df["sin_week"] = np.sin(2*np.pi*df["weekofyear"]/52.0)
    df["cos_week"] = np.cos(2*np.pi*df["weekofyear"]/52.0)
    return df

def main():
    if not DATA.exists():
        raise FileNotFoundError(DATA)
    df = pd.read_csv(DATA, parse_dates=["target_date"])
    # 안전: 열 이름 통일
    df["store_id"] = df["store_id"].astype("Int64")
    df["warehouse_id"] = df["warehouse_id"].astype("Int64")
    df["sku_id"] = df["sku_id"].astype(str)
    df["actual_order_qty"] = pd.to_numeric(df["sku_qty"], errors="coerce").fillna(0).astype(int)

    # 주차 리샘플(누락 주는 0으로 채움) : SKU×Store×Warehouse 단위
    keys = ["warehouse_id", "store_id", "sku_id"]
    df = df[["target_date", *keys, "actual_order_qty"]].copy()
    df["target_date"] = pd.to_datetime(df["target_date"])

    all_frames = []
    for (w, s, k), g in df.groupby(keys):
        g = g.set_index("target_date").sort_index()
        # 주차 캘린더로 리샘플
        g = g.resample(FORECAST_FREQ).sum()
        g[keys] = (w, s, k)
        g = g.reset_index()
        all_frames.append(g)
    dfw = pd.concat(all_frames, ignore_index=True)

    # 라그/이동평균
    dfw = dfw.sort_values(keys + ["target_date"])
    for lag in LAGS:
        dfw[f"lag_{lag}"] = dfw.groupby(keys)["actual_order_qty"].shift(lag)
    for ma in MAS:
        dfw[f"ma_{ma}"] = (
            dfw.groupby(keys)["actual_order_qty"]
               .transform(lambda x: x.shift(1).rolling(ma, min_periods=1).mean())
        )

    # 간단한 프로모션 플래그(있다면 활용): share_norm 급증 등을 통한 힌트
    # 없으면 0
    if "share_norm" in df.columns:
        tmp = df[["target_date", *keys, "share_norm"]].copy()
        tmp = tmp.groupby(["target_date", *keys], as_index=False)["share_norm"].mean()
        dfw = dfw.merge(tmp, on=["target_date", *keys], how="left")
        dfw["share_norm"] = dfw["share_norm"].fillna(0.0)
        dfw["promo_flag"] = (dfw["share_norm"] > 0.25).astype(int)  # 임계값은 도메인에 맞춰 조정
    else:
        dfw["promo_flag"] = 0

    # 시간 피처
    dfw = add_time_features(dfw)

    # 학습 타겟
    dfw["y"] = dfw["actual_order_qty"].astype(float)

    # 최소 이력 필터
    cnt = dfw.groupby(keys)["y"].transform("count")
    dfw = dfw[cnt >= MIN_HISTORY_WEEKS].copy()

    # train/ test split (최근 52주 테스트)
    dfw = dfw.sort_values(keys + ["target_date"]).reset_index(drop=True)
    # 시계열별로 끝에서 52주를 test로
    def assign_split(g):
        g = g.sort_values("target_date")
        if len(g) <= TEST_WEEKS:
            g["split"] = "train"  # 너무 짧으면 전부 train(모델 안정화용)
        else:
            g.loc[g.index[-TEST_WEEKS:], "split"] = "test"
            g["split"] = g["split"].fillna("train")
        return g

    dfw = dfw.groupby(keys, group_keys=False, as_index=False).apply(
    assign_split, include_groups=False
    )

    # 저장
    dfw.to_csv(OUT_ALL, index=False)
    dfw[dfw["split"]=="train"].to_csv(OUT_TR, index=False)
    dfw[dfw["split"]=="test"].to_csv(OUT_TE, index=False)

    print(f"saved: {OUT_ALL.name} ({len(dfw):,}) / train={len(dfw[dfw.split=='train']):,} / test={len(dfw[dfw.split=='test']):,}")

if __name__ == "__main__":
    main()
