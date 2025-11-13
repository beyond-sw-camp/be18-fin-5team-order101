package com.synerge.order101.order.model.entity;

import com.synerge.order101.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "store_order_detail")
public class  StoreOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_detail_id")
    private Long storeOrderDetailId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="store_order_id")
    StoreOrder storeOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "order_qty", nullable = false, precision = 15, scale = 3)
    private BigDecimal orderQty;

    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) {
                createdAt = now;
        }

        this.createdAt = LocalDateTime.now();
    }

    public StoreOrderDetail(StoreOrder storeOrder, Product product, BigDecimal orderQty, BigDecimal unitPrice, BigDecimal amount) {
        this.storeOrder = storeOrder;
        this.product = product;
        this.orderQty = orderQty;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }
}


