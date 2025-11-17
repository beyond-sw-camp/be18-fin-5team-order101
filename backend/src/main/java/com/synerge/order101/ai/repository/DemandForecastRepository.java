package com.synerge.order101.ai.repository;

import com.synerge.order101.ai.model.entity.DemandForecast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DemandForecastRepository extends JpaRepository<DemandForecast, Long> {
    List<DemandForecast> findByTargetWeek(LocalDate targetWeek);

    List<DemandForecast> findByStore_StoreIdAndTargetWeek(Long storeId, LocalDate targetWeek);

    List<DemandForecast> findBySnapshotAtBetween(LocalDateTime start, LocalDateTime end);

    List<DemandForecast> findDistinctBySnapshotAtIsNotNullOrderBySnapshotAtDesc();
}

