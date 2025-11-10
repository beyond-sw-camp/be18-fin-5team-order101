# -*- coding: utf-8 -*-
from pathlib import Path
import pandas as pd
import numpy as np
from sklearn.metrics import mean_absolute_error
from lightgbm import LGBMRegressor

# modeling/ 에서 한 단계 올라가면 app/
BASE = Path(__file__).resolve().parents[1] / "data_pipeline"
TR = BASE / "features_train.csv"
TE = BASE / "features_test.csv"
OUT = BASE / "predictions.csv"

ID_KEYS = ["warehouse_id","store_id","sku_id","target_date"]

def smape(y_true, y_pred):
    denom = (np.abs(y_true) + np.abs(y_pred)) / 2.0
    diff  = np.abs(y_true - y_pred)
    denom = np.where(denom==0, 1.0, denom)
    return np.mean(diff / denom) * 100.0

def mape(y_true, y_pred):
    denom = np.where(y_true==0, 1.0, y_true)
    return (np.abs((y_true - y_pred) / denom)).mean() * 100.0

def main():
    print("DATA DIR:", BASE)  # 경로 확인용
    tr = pd.read_csv(TR, parse_dates=["target_date"])
    te = pd.read_csv(TE, parse_dates=["target_date"])

    target = "y"
    ignore = set(ID_KEYS + [target, "split"])
    features = [c for c in tr.columns if c not in ignore]

    # NaN 처리
    for c in features:
        if tr[c].dtype.kind in "fi":
            tr[c] = tr[c].fillna(0.0); te[c] = te[c].fillna(0.0)
        else:
            tr[c] = tr[c].fillna(0);   te[c] = te[c].fillna(0)

    model = LGBMRegressor(
        n_estimators=1200, learning_rate=0.03,
        subsample=0.9, colsample_bytree=0.8,
        max_depth=-1, num_leaves=63, random_state=42
    )
    model.fit(tr[features], tr[target])

    te_pred = model.predict(te[features])
    te_pred = np.clip(te_pred, 0, None)

    from sklearn.metrics import mean_absolute_error
    mae   = mean_absolute_error(te[target], te_pred)
    _mape = mape(te[target].values, te_pred)
    _smape= smape(te[target].values, te_pred)
    print(f"MAE={mae:,.3f} | MAPE={_mape:,.2f}% | SMAPE={_smape:,.2f}% (n={len(te)})")

    
    keep_ids = [c for c in ["warehouse_id","store_id","sku_id","target_date"] if c in te.columns]
    out = te[keep_ids + [target]].copy()
    if set(["warehouse_id","store_id","sku_id"]).difference(te.columns):
        print("features_test.csv에 일부 ID 컬럼이 없어, 있는 컬럼만 저장")


    out["y_pred"] = te_pred.round(0).astype(int)
    out.to_csv(OUT, index=False)
    print(f"saved: {OUT} ({len(out):,} rows)")

if __name__ == "__main__":
    main()
