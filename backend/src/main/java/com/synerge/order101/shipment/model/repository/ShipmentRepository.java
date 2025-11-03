package com.synerge.order101.shipment.model.repository;

import com.synerge.order101.shipment.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
}
