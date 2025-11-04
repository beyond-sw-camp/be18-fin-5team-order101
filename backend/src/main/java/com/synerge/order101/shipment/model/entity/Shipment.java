package com.synerge.order101.shipment.model.entity;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.outbound.model.entity.OutBound;
import com.synerge.order101.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shipmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outbound_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private OutBound outbound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StoreOrder storeOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = true)
    private ShipmentStatus shipmentStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;



}
