package com.synerge.order101.warehouse.model.entity;

import com.synerge.order101.warehouse.model.ChangeType;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "warehouse_inventory_ledger")
public class WarehouseInventoryLedger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryLedgerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private WarehouseInventory inventoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChangeType changeType;

    @Column(name = "change_qty", nullable = false)
    private Integer changeQuantity;

    @Column(columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String createdBy;
}
