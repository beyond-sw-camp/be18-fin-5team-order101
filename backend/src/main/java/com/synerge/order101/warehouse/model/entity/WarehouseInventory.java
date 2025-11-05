package com.synerge.order101.warehouse.model.entity;


import com.synerge.order101.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "warehouse_inventory")
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouseId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product productId;

    @Column(name = "on_hand_qty", nullable = false)
    private Integer onHandQuantity;

    @Column(name = "safety_qty")
    private Integer safetyQuantity;

    @Column(columnDefinition = "DATETIME(6)")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
