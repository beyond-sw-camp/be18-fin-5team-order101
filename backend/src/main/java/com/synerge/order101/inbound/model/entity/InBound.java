package com.synerge.order101.inbound.model.entity;


import com.synerge.order101.supplier.model.entity.Supplier;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "inbound")
public class InBound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Warehouse warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Supplier supplierId;

    @Column(nullable = false)
    private String inboundNo;

    @Column(name = "inbound_datetime" ,columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    private LocalDateTime inboundDatetime;

}
