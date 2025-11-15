package com.synerge.order101.ai.service;

import com.synerge.order101.ai.exception.AiErrorCode;
import com.synerge.order101.ai.model.dto.response.AiJobTriggerResponseDto;
import com.synerge.order101.ai.model.dto.response.AiMetricResponseDto;
import com.synerge.order101.ai.model.dto.response.DemandForecastResponseDto;
import com.synerge.order101.ai.model.entity.DemandForecast;
import com.synerge.order101.ai.repository.DemandForecastRepository;
import com.synerge.order101.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DemandForecastService {
    private final DemandForecastRepository demandForecastRepository;

    @Transactional
    public AiJobTriggerResponseDto triggerForecast(LocalDate targetWeek){
        // TODO
        //파이썬 AI 서버 호출
        //webclient/TestTemplate등 사용
        //결과를 CSV/JSON을 받아서 수요예측 엔티티로 저장

        return AiJobTriggerResponseDto.builder()
                .jobType("FORECAST")
                .status("ACCEPTED")
                .message("예측 작업 큐에 등록 완. targetWeek = " + targetWeek)
                .build();
    }


    //모델 재학습
    @Transactional
    public AiJobTriggerResponseDto triggerRetrain(){
        //TODO
        //파이썬 서버에 재학습 요청
        return AiJobTriggerResponseDto.builder()
                .jobType("RETRAIN")
                .status("ACCEPTED")
                .message("모델 재학습 작업 큐에 등록 완.")
                .build();
    }


    public List<LocalDateTime> getSnapshotList() {
        return demandForecastRepository
                .findDistinctBySnapshotAtIsNotNullOrderBySnapshotAtDesc()
                .stream()
                .map(DemandForecast::getSnapshotAt)
                .distinct()
                .toList();
    }

    public List<DemandForecastResponseDto> getForecasts(LocalDate targetWeek) {
        List<DemandForecast> list = (targetWeek == null)
                ? demandForecastRepository.findAll()
                : demandForecastRepository.findByTargetWeek(targetWeek);

        return list.stream()
                .map(this::toResponse)
                .toList();
    }

    public DemandForecastResponseDto getForecast(Long id) {
        DemandForecast entity = demandForecastRepository.findById(id)
                .orElseThrow(() -> new CustomException(AiErrorCode.FORECAST_NOT_FOUND));
        return toResponse(entity);
    }





    private DemandForecastResponseDto toResponse(DemandForecast df) {
        return DemandForecastResponseDto.builder()
                .id(df.getDemandForecastId())
                .warehouseId(df.getWarehouse().getWarehouseId())
                .storeId(df.getStore().getStoreId())
                .productId(df.getProduct().getProductId())
                .targetWeek(df.getTargetWeek())
                .predictedQty(df.getYPred())
                .actualOrderQty(df.getActualOrderQty())
                .mape(df.getMape())
                .externalFactorsJson(df.getExternalFactors())
                .snapshotAt(df.getSnapshotAt())
                .updatedAt(df.getUpdatedAt())
                .build();
    }



    public AiMetricResponseDto getMetrics() {
        List<DemandForecast> all = demandForecastRepository.findAll();

        double mae = 0.0;
        double mape = 0.0;
        double smape = 0.0;
        int n = 0;

        for (DemandForecast df : all) {
            if (df.getActualOrderQty() == null || df.getYPred() == null) continue;
            double y = df.getActualOrderQty();
            double yhat = df.getYPred();

            double absErr = Math.abs(y - yhat);
            mae += absErr;
            if (y != 0) {
                mape += absErr / Math.abs(y);
            }
            smape += absErr / ((Math.abs(y) + Math.abs(yhat)) / 2.0);
            n++;
        }

        if (n == 0) {
            return AiMetricResponseDto.builder().mae(0.0).mape(0.0).smape(0.0).build();
        }

        mae /= n;
        mape = mape / n * 100.0;
        smape = smape / n * 100.0;

        return AiMetricResponseDto.builder()
                .mae(mae)
                .mape(mape)
                .smape(smape)
                .build();
    }





}
