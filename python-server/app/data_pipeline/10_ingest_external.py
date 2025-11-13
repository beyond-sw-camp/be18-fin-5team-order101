"""
10_ingest_external.py
- 입력:
  external_weather_weekly.csv     (region,target_date,avg_temp_c,cdd,hdd,precip_mm,heat_wave,cold_wave)
  external_himart_quarterly.csv   (region,quarter_end,himart_sales_krw,himart_sales_index)
  external_events_weekly.csv      (region,target_date,holiday_index,seasonal_peak,marketing_score,back_to_school)
- 처리:
  * 스키마 검증 + 타입/날짜 정리
  * 분기지표(하이마트) → 주차로 forward-fill
  * 세 소스 모두 region+target_date 로 left-join
- 출력: external_factors.csv
"""
from pathlib import Path
import pandas as pd
import numpy as np

BASE = Path(__file__).resolve().parent
WEEKLY_WEATHER = BASE / "external_weather_weekly.csv"
QUARTER_HIMART = BASE / "external_himart_quarterly.csv"
WEEKLY_EVENTS  = BASE / "external_events_weekly.csv"
OUT            = BASE / "external_factors.csv"

def _need(f: Path):
    if not f.exists():
        raise FileNotFoundError(f"필수 파일이 없습니다: {f}")

def _to_week_start_monday(dt: pd.Series) -> pd.Series:
    # dt를 해당 주의 월요일 날짜로 맞춤
    dt = pd.to_datetime(dt)
    return (dt - pd.to_timedelta((dt.dt.weekday) % 7, unit="D")).dt.normalize()

def _validate_cols(df: pd.DataFrame, required: list, name: str):
    miss = [c for c in required if c not in df.columns]
    if miss:
        raise ValueError(f"{name}에 필요한 컬럼이 없습니다: {miss}\n현재 컬럼: {list(df.columns)}")

def _load_weather() -> pd.DataFrame:
    _need(WEEKLY_WEATHER)
    w = pd.read_csv(WEEKLY_WEATHER, dtype={"region": str}, low_memory=False)
    _validate_cols(
        w,
        ["region","target_date","avg_temp_c","cdd","hdd","precip_mm","heat_wave","cold_wave"],
        "external_weather_weekly.csv",
    )
    w["target_date"] = _to_week_start_monday(w["target_date"])
    # 숫자화
    for c in ["avg_temp_c","cdd","hdd","precip_mm","heat_wave","cold_wave"]:
        w[c] = pd.to_numeric(w[c], errors="coerce").fillna(0.0)
    return (w.groupby(["region","target_date"], as_index=False)
             .agg({"avg_temp_c":"mean","cdd":"mean","hdd":"mean",
                   "precip_mm":"sum","heat_wave":"max","cold_wave":"max"}))

def _load_himart() -> pd.DataFrame:
    _need(QUARTER_HIMART)
    h = pd.read_csv(QUARTER_HIMART, dtype={"region": str}, low_memory=False)
    _validate_cols(
        h,
        ["region","quarter_end","himart_sales_krw","himart_sales_index"],
        "external_himart_quarterly.csv",
    )
    h["quarter_end"] = pd.to_datetime(h["quarter_end"])
    # 분기 → 주: 분기 값들을 해당 분기 종료일까지 forward-fill
    # 주 달력 생성
    regions = h["region"].dropna().unique().tolist() or ["본사창고"]
    start = h["quarter_end"].min() - pd.offsets.QuarterEnd(1)  # 한 분기 앞부터
    end   = h["quarter_end"].max() + pd.offsets.QuarterEnd(1)
    weeks = pd.date_range(start=start, end=end, freq="W-MON")
    frames = []
    for r in regions:
        hh = h[h["region"]==r].sort_values("quarter_end").copy()
        # 분기 끝일자를 기준으로 값 유지(quarter_end 이후 다음 quarter_end 전까지 같은 값)
        # 주 달력과 합치기
        base = pd.DataFrame({"region": r, "target_date": weeks})
        # quarter_end를 해당 주의 월요일로 매핑, 그 주부터 next 이전까지 값 유지
        hh["target_date"] = _to_week_start_monday(hh["quarter_end"])
        base = base.merge(hh[["target_date","himart_sales_krw","himart_sales_index"]],
                          on="target_date", how="left")
        base[["himart_sales_krw","himart_sales_index"]] = (
            base[["himart_sales_krw","himart_sales_index"]]
              .ffill().bfill()
        )
        frames.append(base)
    out = pd.concat(frames, ignore_index=True)
    # 숫자화
    out["himart_sales_krw"]   = pd.to_numeric(out["himart_sales_krw"], errors="coerce").fillna(0.0)
    out["himart_sales_index"] = pd.to_numeric(out["himart_sales_index"], errors="coerce").fillna(0.0)
    return out

def _load_events() -> pd.DataFrame:
    _need(WEEKLY_EVENTS)
    e = pd.read_csv(WEEKLY_EVENTS, dtype={"region": str}, low_memory=False)
    _validate_cols(
        e,
        ["region","target_date","holiday_index","seasonal_peak","marketing_score","back_to_school"],
        "external_events_weekly.csv",
    )
    e["target_date"] = _to_week_start_monday(e["target_date"])
    for c in ["holiday_index","marketing_score"]:
        e[c] = pd.to_numeric(e[c], errors="coerce").fillna(0.0)
    for c in ["seasonal_peak","back_to_school"]:
        e[c] = pd.to_numeric(e[c], errors="coerce").fillna(0).astype(int)
    # 주/지역 중복 있을 수 있어 평균/최대 규칙으로 집계
    e = (e.groupby(["region","target_date"], as_index=False)
           .agg({"holiday_index":"mean","marketing_score":"mean",
                 "seasonal_peak":"max","back_to_school":"max"}))
    return e

def main():
    w = _load_weather()
    h = _load_himart()
    e = _load_events()

    ext = (w.merge(h, on=["region","target_date"], how="left")
             .merge(e, on=["region","target_date"], how="left"))

    # 결측 기본값
    fill_0 = ["himart_sales_krw","himart_sales_index",
              "holiday_index","marketing_score","seasonal_peak","back_to_school"]
    for c in fill_0:
        if c in ext.columns:
            ext[c] = ext[c].fillna(0.0 if ext[c].dtype.kind in "fi" else 0)

    # 파생
    for k in ["avg_temp_c","cdd","hdd","precip_mm","himart_sales_index","holiday_index","marketing_score"]:
        if k in ext.columns:
            ext[f"{k}_ma4"] = (ext.sort_values(["region","target_date"])
                                  .groupby("region")[k]
                                  .transform(lambda s: s.rolling(4, min_periods=1).mean()))

    ext = ext.sort_values(["region","target_date"]).reset_index(drop=True)
    ext.to_csv(OUT, index=False, encoding="utf-8-sig")
    print(f"saved: {OUT} ({len(ext):,} rows)")

if __name__ == "__main__":
    main()
