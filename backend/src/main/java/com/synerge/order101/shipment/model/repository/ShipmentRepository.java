package com.synerge.order101.shipment.model.repository;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("""
    select s
      from Shipment s
     where s.shipmentStatus = :status
       and s.inventoryApplied = false
  """)
    List<Shipment> findByStatusAndNotApplied(@Param("status") ShipmentStatus status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Shipment s
       set s.shipmentStatus = :nextStatus,
           s.updatedAt      = :now
     where s.shipmentStatus = :currentStatus
       and s.createdAt      <= :threshold
  """)
    int updateFromCreatedAt(@Param("currentStatus") ShipmentStatus currentStatus,
                            @Param("nextStatus")   ShipmentStatus nextStatus,
                            @Param("threshold")    LocalDateTime threshold,
                            @Param("now")          LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    update Shipment s
       set s.shipmentStatus = :nextStatus,
           s.updatedAt      = :now
     where s.shipmentStatus = :currentStatus
       and s.updatedAt      <= :threshold
  """)
    int updateFromUpdatedAt(@Param("currentStatus") ShipmentStatus currentStatus,
                            @Param("nextStatus")   ShipmentStatus nextStatus,
                            @Param("threshold")    LocalDateTime threshold,
                            @Param("now")          LocalDateTime now);
}
