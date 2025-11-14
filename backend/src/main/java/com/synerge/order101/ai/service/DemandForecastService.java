package com.synerge.order101.ai.service;

import com.synerge.order101.ai.model.dto.response.AiJobTriggerResponse;
import com.synerge.order101.ai.model.entity.DemandForecast;
import com.synerge.order101.ai.repository.DemandForecastRepository;
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
    public AiJobTriggerResponse triggerForecast(LocalDate targetWeek){
        // TODO
        //파이썬 AI 서버 호출
        //webclient/TestTemplate등 사용
        //결과를 CSV/JSON을 받아서 수요예측 엔티티로 저장

        return AiJobTriggerResponse.builder()
                .jobType("FORECAST")
                .status("ACCEPTED")
                .message("예측 작업 큐에 등록 완. targetWeek = " + targetWeek)
                .build();
    }


    //모델 재학습
    @Transactional
    public AiJobTriggerResponse triggerRetrain(){
        //TODO
        //파이썬 서버에 재학습 요청
        return AiJobTriggerResponse.builder()
                .jobType("RETRAIN")
                .status("ACCEPTED")
                .message("모델 재학습 작업 큐에 등록 완.")
                .build();
    }


    public List<LocalDateTime> getSnapshotList() {
        return demandForecastRepository
                .findDistinctBySnapshotAt()
                .stream()
                .map(DemandForecast::getSnapshotAt)
                .distinct()
                .toList();
    }
}
