# -*- coding: utf-8 -*-
from pathlib import Path
import json
import numpy as np
import pandas as pd
from lightgbm import LGBMRegressor, early_stopping, log_evaluation
from sklearn.metrics import mean_absolute_error
import joblib

BASE = Path(__file__).resolve().parents[1] / "data_pipeline"
TR = BASE / "features_train.csv"
TE = BASE / "features_test.csv"
OUT_PRED = BASE / "predictions.csv"
MODEL_PATH = BASE / "lightgbm_model.pkl"
FEATS_JSON = BASE / "lightgbm_features.json"
METRICS_CSV = BASE / "metrics_eval.csv"

ID_KEYS = ["warehouse_id","store_id","sku_id","region","target_date","split"]
TARGET = "y"
VAL_WEEKS = 12  # 각 (wh, store, sku) 시계열의 마지막 12주를 검증으로 사용
LEAKY = {"actual_order_qty", "share_norm", "promo_flag"}  # 누수 위험 피처

def smape(y_true, y_pred):
    denom = (np.abs(y_true) + np.abs(y_pred)) / 2.0
    diff  = np.abs(y_true - y_pred)
    denom = np.where(denom==0, 1.0, denom)
    return np.mean(diff / denom) * 100.0

def mape(y_true, y_pred):
    denom = np.where(y_true==0, 1.0, y_true)
    return (np.abs((y_true - y_pred) / denom)).mean() * 100.0

def add_prev_flags(df: pd.DataFrame) -> pd.DataFrame:
    # 현재주의 promo_flag는 누수 위험 → 이전주 값만 파생해서 사용
    if "promo_flag" in df.columns:
        df["promo_flag_prev"] = (
            df.groupby(["warehouse_id","store_id","sku_id"])["promo_flag"]
              .shift(1).fillna(0)
        )
    return df

def build_features(df: pd.DataFrame) -> list:
    ignore = set(ID_KEYS + [TARGET, "actual_order_qty"])
    numeric_cols = set(df.select_dtypes(include=["number","bool"]).columns.tolist())
    feats = [c for c in df.columns if c in numeric_cols and c not in ignore and c not in LEAKY]
    if "promo_flag_prev" in df.columns and "promo_flag_prev" not in feats:
        feats.append("promo_flag_prev")
    # 중복 제거
    feats = list(dict.fromkeys(feats))
    return feats

def split_train_val(tr: pd.DataFrame) -> tuple[pd.DataFrame, pd.DataFrame]:
    keys = ["warehouse_id","store_id","sku_id"]
    tr = tr.sort_values(keys + ["target_date"]).copy()
    # 그룹 내 순번(0..n-1)과 크기 계산
    tr["idx_in_grp"] = tr.groupby(keys).cumcount()
    tr["grp_size"]   = tr.groupby(keys)[TARGET].transform("size")
    tr["is_val"]     = (tr["grp_size"] - tr["idx_in_grp"]) <= VAL_WEEKS
    tr_fit = tr.loc[~tr["is_val"]].drop(columns=["idx_in_grp","grp_size","is_val"])
    tr_val = tr.loc[ tr["is_val"]].drop(columns=["idx_in_grp","grp_size","is_val"])
    return tr_fit, tr_val

def main():
    print("DATA DIR:", BASE)
    tr = pd.read_csv(TR, parse_dates=["target_date"])
    te = pd.read_csv(TE, parse_dates=["target_date"])

    tr = add_prev_flags(tr)
    te = add_prev_flags(te)

    features = build_features(tr)
    print(f"[DEBUG] using {len(features)} features:", features)

    # NaN 채우기
    for c in features:
        if tr[c].dtype.kind in "fi":
            tr[c] = tr[c].fillna(0.0); te[c] = te[c].fillna(0.0)
        else:
            tr[c] = tr[c].fillna(0);   te[c] = te[c].fillna(0)

    # Train / Val 분리(시계열 마지막 12주 검증)
    tr_fit, tr_val = split_train_val(tr)
    X_tr, y_tr = tr_fit[features], tr_fit[TARGET]
    X_val, y_val = tr_val[features], tr_val[TARGET]

    # 규제 + 조기중지(콜백 방식)
    model = LGBMRegressor(
        n_estimators=4000,
        learning_rate=0.01,
        num_leaves=31,
        max_depth=8,
        subsample=0.8,
        colsample_bytree=0.8,
        reg_alpha=0.5,
        reg_lambda=0.7,
        random_state=42
    )
    model.fit(
        X_tr, y_tr,
        eval_set=[(X_val, y_val)],
        eval_metric="mae",
        callbacks=[
            early_stopping(stopping_rounds=300, first_metric_only=True),
            log_evaluation(period=200)
        ]
    )

    # 테스트 예측(25년 포함 전체 test split)
    best_iter = getattr(model, "best_iteration_", None)
    te_pred = np.clip(model.predict(te[features], num_iteration=best_iter), 0, None)
    mae   = mean_absolute_error(te[TARGET], te_pred)
    _mape = mape(te[TARGET].values, te_pred)
    _smape= smape(te[TARGET].values, te_pred)
    print(f"MAE={mae:,.3f} | MAPE={_mape:,.2f}% | SMAPE={_smape:,.2f}% (n={len(te)})")

    # 예측 저장
    keep_ids = [c for c in ["warehouse_id","store_id","sku_id","region","target_date"] if c in te.columns]
    out = te[keep_ids + [TARGET]].copy()
    out["y_pred"] = te_pred.round(0).astype(int)
    out.to_csv(OUT_PRED, index=False)
    print(f"saved: {OUT_PRED} ({len(out):,} rows)")

    # 모델/피처/메트릭 저장
    joblib.dump(model, MODEL_PATH)
    with open(FEATS_JSON, "w", encoding="utf-8") as f:
        json.dump(features, f, ensure_ascii=False, indent=2)
    pd.DataFrame({
        "metric":["mae","mape","smape"],
        "value":[mae, _mape, _smape],
        "best_iteration":[best_iter]*3
    }).to_csv(METRICS_CSV, index=False)

    print(f"model saved to {MODEL_PATH}")
    print(f"best_iteration: {best_iter}")

if __name__ == "__main__":
    main()
