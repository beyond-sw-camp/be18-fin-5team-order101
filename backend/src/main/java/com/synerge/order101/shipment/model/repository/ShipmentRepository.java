package com.synerge.order101.shipment.model.repository;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.entity.Shipment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Shipment s
           set s.shipmentStatus = :nextStatus,
               s.updatedAt = :now
         where s.shipmentStatus = :currentStatus
           and s.createdAt <= :threshold
    """)
    int updateStatus(
            @Param("currentStatus") ShipmentStatus currentStatus,
            @Param("nextStatus") ShipmentStatus nextStatus,
            @Param("threshold") LocalDateTime threshold,
            @Param("now") LocalDateTime now
    );



}
