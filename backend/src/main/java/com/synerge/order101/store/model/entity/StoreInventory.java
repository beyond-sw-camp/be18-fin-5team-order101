package com.synerge.order101.store.model.entity;

import com.synerge.order101.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "store_inventory",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "ux_store_inventory_store_product",
                        columnNames = {"store_id", "product_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

    @Column(name = "on_hand_qty", nullable = false)
    private int onHandQty;

    @Column(name = "in_transit_qty", nullable = false)
    private int inTransitQty;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    public static StoreInventory create(Store store, Product product) {
        return StoreInventory.builder()
                .store(store)
                .product(product)
                .onHandQty(0)
                .inTransitQty(0)
                .build();
    }


    public void increaseInTransit(int qty) {
        if (qty <= 0) return;
        this.inTransitQty += qty;
    }

    public void decreaseInTransit(int qty) {
        if (qty <= 0) return;
        this.inTransitQty = Math.max(this.inTransitQty - qty, 0);
    }

    public void increaseOnHand(int qty) {
        if (qty <= 0) return;
        this.onHandQty += qty;
    }

    public void decreaseOnHand(int qty) {
        if (qty <= 0) return;
        this.onHandQty = Math.max(this.onHandQty - qty, 0);
    }

    public void moveTransitToOnHand(int qty) {
        if (qty <= 0) return;
        decreaseInTransit(qty);
        increaseOnHand(qty);
    }

}
