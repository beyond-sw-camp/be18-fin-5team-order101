from dotenv import load_dotenv
import os, io, time
from datetime import datetime, timedelta
import requests
import pandas as pd
import numpy as np


load_dotenv()
SERVICE_KEY = os.getenv("SERVICE_KEY")   
STATION_ID  = "108"                    
START_DATE  = "20210101"                
END_DATE    = "20251231"              
CDD_BASE    = 24.0
HDD_BASE    = 18.0
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
OUTPUT_PATH = os.path.join(BASE_DIR, "external_weather_weekly.csv")


ASOS_URL = "http://apis.data.go.kr/1360000/AsosDalyInfoService/getWthrDataList"


def month_iter(start_dt: datetime, end_dt: datetime):
    """start~end를 월 단위 구간으로 분할"""
    cur = start_dt.replace(day=1)
    while cur <= end_dt:
        y, m = cur.year, cur.month
        if m == 12:
            month_end = cur.replace(year=y+1, month=1, day=1) - timedelta(days=1)
        else:
            month_end = cur.replace(month=m+1, day=1) - timedelta(days=1)
        yield max(cur, start_dt), min(month_end, end_dt)
        cur = (month_end + timedelta(days=1)).replace(day=1)

def safe_to_numeric(series):
    return pd.to_numeric(series, errors="coerce")

def to_monday(d: pd.Timestamp):
    return (d - pd.to_timedelta(d.weekday(), unit="D")).date()

def compute_degree_days(avg_t: float, cdd_base=24.0, hdd_base=18.0):
    if pd.isna(avg_t):
        return 0.0, 0.0
    cdd = max(0.0, float(avg_t - cdd_base))
    hdd = max(0.0, float(hdd_base - avg_t))
    return cdd, hdd

def flag_heat_wave(week_df: pd.DataFrame) -> int:
    """주중 tmax ≥ 33°C 연속 2일 이상"""
    mx = (week_df["ta_max"] >= 33.0).astype(int).to_numpy()
    return 1 if any(a == 1 and b == 1 for a, b in zip(mx, mx[1:])) else 0

def flag_cold_wave(week_df: pd.DataFrame) -> int:
    """주중 tmin ≤ -12°C 연속 2일 이상 또는 전일 대비 최저 10°C 급락 & 해당일 최저 ≤ 3°C"""
    mn = (week_df["ta_min"] <= -12.0).astype(int).to_numpy()
    if any(a == 1 and b == 1 for a, b in zip(mn, mn[1:])):
        return 1
    tmin = week_df["ta_min"].to_numpy()
    for i in range(1, len(tmin)):
        if not (np.isnan(tmin[i]) or np.isnan(tmin[i-1])):
            if (tmin[i-1] - tmin[i] >= 10.0) and (tmin[i] <= 3.0):
                return 1
    return 0


def fetch_asos_daily_json(stn: str, start: str, end: str) -> pd.DataFrame:
    if not SERVICE_KEY:
        raise ValueError("환경변수 SERVICE_KEY 없음")

    start_dt = datetime.strptime(start, "%Y%m%d")
    end_dt   = datetime.strptime(end,   "%Y%m%d")

    frames = []
    for sdt, edt in month_iter(start_dt, end_dt):
        page = 1
        while True:
            params = {
                "serviceKey": SERVICE_KEY,
                "dataType": "JSON",
                "dataCd": "ASOS",
                "dateCd": "DAY",
                "startDt": sdt.strftime("%Y%m%d"),
                "endDt": edt.strftime("%Y%m%d"),
                "stnIds": stn,
                "numOfRows": "999",
                "pageNo": str(page),
            }

            # 간단 재시도
            for attempt in range(3):
                try:
                    r = requests.get(ASOS_URL, params=params, timeout=30)
                    r.raise_for_status()
                    break
                except requests.RequestException:
                    if attempt == 2:
                        raise
                    time.sleep(1 + attempt)

            data = r.json()
            body = (data.get("response") or {}).get("body") or {}
            items = ((body.get("items") or {}).get("item") or [])
            total = body.get("totalCount", 0) or 0

            if not items:
                break

            df = pd.DataFrame(items)
            df = df.rename(columns={
                "tm": "date",
                "avgTa": "ta_avg",
                "maxTa": "ta_max",
                "minTa": "ta_min",
                "sumRn": "sum_rn"
            })

            # 필수 컬럼 보정
            for col in ["date","ta_avg","ta_max","ta_min","sum_rn"]:
                if col not in df.columns:
                    df[col] = np.nan

            # 타입 변환
            df["date"] = pd.to_datetime(df["date"], errors="coerce")
            for c in ["ta_avg","ta_max","ta_min","sum_rn"]:
                df[c] = safe_to_numeric(df[c])

            frames.append(df[["date","ta_avg","ta_max","ta_min","sum_rn"]])

            # 페이지 계산
            if len(items) < 999:
                break
            page += 1

    if not frames:
        return pd.DataFrame(columns=["date","ta_avg","ta_max","ta_min","sum_rn"])

    out = pd.concat(frames, ignore_index=True).sort_values("date")
    out = out[(out["date"] >= start_dt) & (out["date"] <= end_dt)].reset_index(drop=True)
    return out


def main():
    daily = fetch_asos_daily_json(STATION_ID, START_DATE, END_DATE)
    if daily.empty:
        raise RuntimeError("일자료 수집 결과가 비었습니다.  STATION_ID/기간/키를 확인하세요.")


    daily["sum_rn"] = daily["sum_rn"].fillna(0.0)

    dd = daily.copy()
    dd[["cdd","hdd"]] = dd["ta_avg"].apply(
        lambda x: pd.Series(compute_degree_days(x, CDD_BASE, HDD_BASE))
    )


    dd["week_start"] = dd["date"].apply(to_monday)


    agg = dd.groupby("week_start").agg(
        avg_temp_c=("ta_avg","mean"),
        cdd=("cdd","sum"),
        hdd=("hdd","sum"),
        precip_mm=("sum_rn","sum"),
        heat_wave=("ta_max", lambda s: flag_heat_wave(dd.loc[s.index, ["ta_max","ta_min"]])),
        cold_wave=("ta_min", lambda s: flag_cold_wave(dd.loc[s.index, ["ta_max","ta_min"]]))
    ).reset_index()


    agg["avg_temp_c"] = agg["avg_temp_c"].round(1)
    agg["cdd"]        = agg["cdd"].round(1)
    agg["hdd"]        = agg["hdd"].round(1)
    agg["precip_mm"]  = agg["precip_mm"].fillna(0).round(1)
    agg["heat_wave"]  = agg["heat_wave"].astype(int)
    agg["cold_wave"]  = agg["cold_wave"].astype(int)


    out = agg.rename(columns={"week_start":"target_date"})[
        ["target_date","avg_temp_c","cdd","hdd","precip_mm","heat_wave","cold_wave"]
    ].copy()
    out.insert(0, "region", "본사창고")
    out["target_date"] = out["target_date"].astype(str)


    out = out.sort_values("target_date").reset_index(drop=True)
    out.to_csv(OUTPUT_PATH, index=False, encoding="utf-8-sig")

    print(out.head(8).to_string(index=False))

if __name__ == "__main__":
    main()
