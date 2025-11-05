package com.synerge.order101.store.model.entity;

import com.synerge.order101.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "store_inventory")
@NoArgsConstructor
@AllArgsConstructor
public class StoreInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_inventory_id")
    private Long storeInventoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "on_hand_qty", precision = 18, scale = 3, nullable = false)
    private BigDecimal onHandQty = BigDecimal.ZERO;

    @Column(name = "in_transit_qty", precision = 18, scale = 3, nullable = false)
    private BigDecimal inTransitQty = BigDecimal.ZERO;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
