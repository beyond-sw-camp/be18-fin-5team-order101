package com.synerge.order101.forecast.model.entity;

import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "smart_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class smartOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smartOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demand_forecast_id")
    private demandForecast demandForecast;

    @Column(name = "target_week", nullable = false)
    private Date targetWeek;

    @Column(name = "recommended_order_qty", nullable = false)
    private int recommendedOrderQty;

    @Column(name = "forecast_qty", nullable = false)
    private int forecastQty;

    @Enumerated(EnumType.STRING)
    @Column(name = "smart_order_status", nullable = false)
    private SmartOrderStatus smartOrderStatus = SmartOrderStatus.NEW;

    @CreationTimestamp
    @Column(name = "snapshot_at")
    private LocalDateTime snapshotAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;




    public enum SmartOrderStatus{
        NEW,
        CONFIRMED,
        REJECTED
    }




}
