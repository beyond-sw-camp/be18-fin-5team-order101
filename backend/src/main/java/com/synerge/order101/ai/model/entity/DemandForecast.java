package com.synerge.order101.ai.model.entity;

import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(
        name = "demand_forecast",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_demand_forecast_snapshot",
                        columnNames = {"warehouse_id", "store_id", "product_id", "target_week", "snapshot_at"}
                )
        },
        indexes = {
                @Index(name = "idx_df_store_product_week", columnList = "store_id, product_id, target_week")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DemandForecast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "demand_forecast_id")
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
    private LocalDate targetWeek;

    @Column(name = "y_pred", nullable = false)
    private Integer yPred;

    @Column(name = "actual_order_qty")
    private Integer actualOrderQty;

    @Column(name = "mape")
    private Integer mape;

    @Lob
    @Column(name = "external_factors", columnDefinition = "json")
    private String externalFactors;


    @CreationTimestamp
    @Column(name = "snapshot_at", nullable = false, updatable = false)
    private LocalDateTime snapshotAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;






}
