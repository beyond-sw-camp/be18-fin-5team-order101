package com.synerge.order101.ai.controller;

import com.synerge.order101.ai.model.dto.response.AiJobTriggerResponseDto;
import com.synerge.order101.ai.model.dto.response.AiMetricResponseDto;
import com.synerge.order101.ai.model.dto.response.DemandForecastResponseDto;
import com.synerge.order101.ai.service.DemandForecastService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DemandForecastController {

    private final DemandForecastService demandForecastService;

    // 수요 옟측 실행 트리거
    @PostMapping("/internal/ai/forecasts")
    public ResponseEntity<AiJobTriggerResponseDto> triggerForecast(
            @RequestParam("targetWeek")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate targetWeek
    ){
        return ResponseEntity.ok(demandForecastService.triggerForecast(targetWeek));
    }

    // 수요 예측 목록 조회
    @GetMapping("/forecasts")
    public ResponseEntity<List<DemandForecastResponseDto>> getForecasts(
            @RequestParam(value = "targetWeek", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate targetWeek
    ){
        return ResponseEntity.ok(demandForecastService.getForecasts(targetWeek));
    }

    // 수요 예측 상세 조회
    @GetMapping("/forecasts/{demandForecastId}")
    public ResponseEntity<DemandForecastResponseDto> getForecast(
            @PathVariable Long demandForecastId
    ){
        return ResponseEntity.ok(demandForecastService.getForecast(demandForecastId));
    }

    // 스냅샷 리스트 조회
    @GetMapping("/forecasts/snapshots")
    public ResponseEntity<List<LocalDateTime>>getSnapshotList(){
        return ResponseEntity.ok(demandForecastService.getSnapshotList());
    }


    //모델 메트릭 조회
    @GetMapping("/internal/ai/metrics")
    public ResponseEntity<AiMetricResponseDto> getMetrics(){
        return ResponseEntity.ok(demandForecastService.getMetrics());
    }



    // 모델 재학습 트리거
    @PostMapping("/internal/ai/model/retrain")
    public ResponseEntity<AiJobTriggerResponseDto> triggerRtrain(){
        return ResponseEntity.ok(demandForecastService.triggerRetrain());
    }


}
