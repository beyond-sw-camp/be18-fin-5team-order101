from pathlib import Path
import csv

BASE = Path(__file__).resolve().parent
SRC  = BASE / "sku_catalog.csv"
TMP  = BASE / "sku_catalog.cleaned.tmp"

def main():
    with SRC.open("r", newline="", encoding="utf-8") as f:
        rdr = csv.reader(f)
        header = next(rdr)
        rows = list(rdr)

    # 기준: 헤더 길이만큼만 유지
    keep_len = len(header)
    fixed = []
    dropped_rows = 0
    for r in rows:
        if len(r) > keep_len:
            dropped_rows += 1
            r = r[:keep_len]
        elif len(r) < keep_len:
            r = r + [""] * (keep_len - len(r))
        fixed.append(r)

    with TMP.open("w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(header)
        w.writerows(fixed)

    # 원본 교체
    SRC.write_text(TMP.read_text(encoding="utf-8"), encoding="utf-8")
    TMP.unlink(missing_ok=True)

    print(f"cleaned: {SRC}")
    print(f"   - header columns: {keep_len}")
    print(f"   - rows with trailing extras removed: {dropped_rows}")

if __name__ == "__main__":
    main()
