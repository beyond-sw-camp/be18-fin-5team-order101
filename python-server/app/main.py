from fastapi import FastAPI
import subprocess
from pathlib import Path

app = FastAPI()
BASE = Path(__file__).resolve().parent


@app.get("/api/v1/ai/train")
def run_full_pipeline():
    scripts = [
        BASE / "data_pipeline" / "02_expand_sku_from_category.py",
        BASE / "data_pipeline" / "05_make_features.py",
        BASE / "modeling" / "07_train_eval.py"
    ]
    logs = []
    for script in scripts:
        try:
            res = subprocess.run(
                ["python", str(script)],
                capture_output=True, text=True, check=True
            )
            logs.append(f"[OK] {script.name}\n{res.stdout}")
        except subprocess.CalledProcessError as e:
            logs.append(f"[ERROR] {script.name}\n{e.stderr}")
            return {"status": "failed", "log": logs}
    return {"status": "success", "log": logs}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)