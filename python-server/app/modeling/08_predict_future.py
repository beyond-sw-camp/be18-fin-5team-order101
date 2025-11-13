from pathlib import Path
import pandas as pd
import numpy as np
import joblib
import json

BASE = Path(__file__).resolve().parents[1] / "data_pipeline"
FEA = BASE / "features_all.csv"
EXT = BASE / "external_factors.csv"
MODEL_PATH = BASE / "lightgbm_model.pkl"
FEATURES_PATH = BASE / "lightgbm_features.json"
OUT = BASE / "forecast_next.csv"
HORIZON_WEEKS = 12
FORECAST_FREQ = "W-MON"
KEYS = ["warehouse_id","store_id","sku_id","region"] 

def _to_week_start_monday(dt: pd.Series) -> pd.Series:
    dt = pd.to_datetime(dt)
    return (dt - pd.to_timedelta(dt.dt.weekday % 7, unit="D")).dt.normalize()

def _add_time_features(df: pd.DataFrame) -> pd.DataFrame:
    dt = pd.to_datetime(df["target_date"])
    df["year"] = dt.dt.year
    df["weekofyear"] = dt.dt.isocalendar().week.astype(int)
    df["month"] = dt.dt.month
    df["sin_week"] = np.sin(2*np.pi*df["weekofyear"]/52.0)
    df["cos_week"] = np.cos(2*np.pi*df["weekofyear"]/52.0)
    return df

def _load_external():
    if not EXT.exists():
        return None
    ext = pd.read_csv(EXT, parse_dates=["target_date"])
    if "region" not in ext.columns:
        ext["region"] = "본사창고"
    ext["target_date"] = _to_week_start_monday(ext["target_date"])
    return ext

def main():
    # 로드
    model = joblib.load(MODEL_PATH)
    with open(FEATURES_PATH, "r", encoding="utf-8") as f:
        features_used = json.load(f)

    fea = pd.read_csv(FEA, parse_dates=["target_date"])
    if "region" not in fea.columns:
        fea["region"] = "본사창고"
    fea["target_date"] = _to_week_start_monday(fea["target_date"])

    # 시계열별 마지막 주 상태 가져오기
    last_dt = fea["target_date"].max()
    start = (last_dt + pd.offsets.Week(weekday=0)) 
    horizon_dates = pd.date_range(start, periods=HORIZON_WEEKS, freq=FORECAST_FREQ)

    # 라그/이동평균에 필요한 최근 이력만 남기기
    lag_cols = [c for c in fea.columns if c.startswith("lag_")]
    ma_cols  = [c for c in fea.columns if c.startswith("ma_")]
    need_cols = ["target_date", "y", *KEYS, *lag_cols, *ma_cols, "promo_flag",
                 "year","weekofyear","month","sin_week","cos_week"]
    need_cols = [c for c in need_cols if c in fea.columns]
    base = fea.sort_values(KEYS+["target_date"]).groupby(KEYS).tail(60).copy()  

    # 외부요인 로드
    ext = _load_external()

    forecasts = []

    # 시계열별 루프
    for grp, g in base.groupby(KEYS):
        g = g.sort_values("target_date").copy()
        last = g.iloc[-1]

        # 시뮬레이션 버퍼: 과거 관측 + 미래 예측을 계속 쌓음
        buf = g[["target_date","y", *lag_cols, *ma_cols, "promo_flag"]].copy()

        for dt in horizon_dates:
            row = {k: v for k, v in zip(KEYS, grp)}
            row["target_date"] = dt

            # 시간 피처
            tmp = pd.DataFrame([row])
            tmp = _add_time_features(tmp)

            # 외부요인 결합
            if ext is not None:
                join = ext[(ext["region"] == row.get("region", "본사창고")) & (ext["target_date"] == dt)]
                if not join.empty:
                    for c in join.columns:
                        if c in ["region","target_date"]:
                            continue
                        tmp[c] = join.iloc[0][c]
                else:
                    for c in ext.columns:
                        if c in ["region","target_date"]:
                            continue
                        tmp[c] = 0.0

            # 라그/MA 업데이트: buf의 마지막 값들로 계산
            # lag_k = 직전 k주의 y
            for c in lag_cols:
                k = int(c.split("_")[1])
                tmp[c] = buf["y"].tail(k).iloc[-k] if len(buf) >= k else np.nan
        
                if len(buf) >= k:
                    tmp[c] = buf["y"].iloc[-k]
                else:
                    tmp[c] = np.nan

            # 이동평균 ma_k = 직전 k주 평균 
            for c in ma_cols:
                k = int(c.split("_")[1])
                tmp[c] = buf["y"].tail(k).mean() if len(buf) >= 1 else np.nan

            

            if "promo_flag" in tmp.columns:
                tmp["promo_flag"] = tmp.get("promo_flag", 0)

            # 모델이 쓰는 피처만 정렬
            X = tmp.reindex(columns=features_used, fill_value=0.0)

            # 예측
            yhat = model.predict(X.values)[0]
            yhat = max(0.0, float(yhat))

            # 결과 저장
            rec = {**row, "y_pred": round(yhat)}
            forecasts.append(rec)


            # 버퍼에 예측치 추가
            buf = pd.concat([buf, pd.DataFrame([{
                "target_date": dt,
                "y": yhat,
                **{c: tmp[c].iloc[0] for c in lag_cols if c in tmp.columns},
                **{c: tmp[c].iloc[0] for c in ma_cols  if c in tmp.columns},
                "promo_flag": tmp.get("promo_flag", pd.Series([0])).iloc[0]
            }])], ignore_index=True)

    out = pd.DataFrame(forecasts)
    out = out.sort_values(KEYS+["target_date"]).reset_index(drop=True)
    out.to_csv(OUT, index=False)
    print(f"saved: {OUT} ({len(out):,} rows)")

if __name__ == "__main__":
    main()