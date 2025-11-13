package com.synerge.order101.forecast.model.entity;

import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "demand_forecast")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class demandForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long demandForecastId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "target_week", nullable = false)
    private Date targetWeek;

    @Column(name = "y_pred", nullable = false)
    private int yPred;

    @Column(name = "actual_order_qty")
    private int actualOrderQty;

    @Column(name = "mape")
    private int mape;

    @Lob
    @Column(name = "external_factors", columnDefinition = "LONGBLOB")
    private byte[] externalFactors;


    @CreationTimestamp
    @Column(name = "snapshot_at")
    private LocalDateTime snapshotAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;






}
