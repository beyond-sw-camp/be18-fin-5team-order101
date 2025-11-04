package com.synerge.order101.shipment.model.repository;

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
           set s.status = :nextStatus,
               s.updatedAt = :now
         where s.status = :currentStatus
           and s.createdAt <= :threshold
    """)
    int updateStatus(
            @Param("currentStatus") Shipment.Status currentStatus,
            @Param("nextStatus") Shipment.Status nextStatus,
            @Param("threshold") LocalDateTime threshold,
            @Param("now") LocalDateTime now
    );



}
