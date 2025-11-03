package com.synerge.order101.shipment.model.repository;

import com.synerge.order101.shipment.model.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment,Long> {
}
