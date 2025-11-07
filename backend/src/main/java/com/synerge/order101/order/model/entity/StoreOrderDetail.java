package com.synerge.order101.order.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "store_order_detail")
public class StoreOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_order_detail_id")
    private Long storeOrderDetailId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="store_order_id")
    StoreOrder storeOrder;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "order_qty", nullable = false, precision = 15, scale = 3)
    private BigDecimal orderQty;

    @Column(name = "unit_price", precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "amount", precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

