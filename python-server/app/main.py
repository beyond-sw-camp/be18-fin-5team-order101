import subprocess
from pathlib import Path
from fastapi import FastAPI, HTTPException
from fastapi.responses import JSONResponse
from datetime import date

from .schemas import ForecastTriggerRequest, ForecastTriggerResponse, RetrainResponse
from .service.forecast_service import run_forecast_pipeline
from .service.train_service import retrain_model
from app.ops.make_product_master_from_sku import main as build_master
from app.service.product_loader import load_product_master_once


# app = FastAPI()

BASE = Path(__file__).resolve().parent
def create_app():
    app = FastAPI()

    build_master()
    load_product_master_once()

    return app


app = create_app()




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





@app.post(
    "/internal/ai/forecasts",
    response_model=ForecastTriggerResponse,
    status_code=202,
)
def trigger_forecasts(req: ForecastTriggerRequest):
    """
    Java to Python: 수요 예측 실행 요청
    body: { "targetWeek": "YYYY-MM-DD" }
    """
    try:
        inserted = run_forecast_pipeline(req.targetWeek)
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Forecast failed: {e}")

    return ForecastTriggerResponse(
        jobType="FORECAST",
        status="ACCEPTED",
        message=f"Inserted {inserted} demand_forecast rows for targetWeek={req.targetWeek}",
    )


@app.post(
    "/internal/ai/model/retrain",
    response_model=RetrainResponse,
    status_code=202,
)
def trigger_retrain():
    """
    Java to Python: 모델 재학습 요청
    """
    try:
        retrain_model()
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Retrain failed: {e}")

    return RetrainResponse(
        jobType="RETRAIN",
        status="ACCEPTED",
        message="Model retrain job completed (or queued).",
    )


@app.get("/health")
def health_check():
    return {"status": "ok"}



if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)