package com.synerge.order101.ai.model.entity;

import com.synerge.order101.ai.exception.AiErrorCode;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(
        name = "smart_order",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_smart_order_forecast",
                        columnNames = {"demand_forecast_id"}
                )
        },
        indexes = {
                @Index(name = "idx_so_store_product_week", columnList = "store_id, product_id, target_week")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SmartOrder {
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
    private DemandForecast demandForecast;

    @Column(name = "target_week", nullable = false)
    private LocalDate targetWeek;

    @Column(name = "recommended_order_qty", nullable = false)
    private int recommendedOrderQty;

    @Column(name = "forecast_qty", nullable = false)
    private int forecastQty;

    @Enumerated(EnumType.STRING)
    @Column(name = "smart_order_status", nullable = false)
    private SmartOrderStatus smartOrderStatus = SmartOrderStatus.DRAFT;

    @CreationTimestamp
    @Column(name = "snapshot_at")
    private LocalDateTime snapshotAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;




    public enum SmartOrderStatus{
        DRAFT,
        SUBMITTED,
        APPROVED,
        REJECTED,
        CANCELLED
    }


    public void updateDraft(Integer newRecommendedQty){
        if(this.smartOrderStatus != SmartOrderStatus.DRAFT){
            throw new CustomException(AiErrorCode.SMART_ORDER_UPDATE_FAILED);
        }
        this.recommendedOrderQty = newRecommendedQty;
    }

    public void submit() {
        if (this.smartOrderStatus != SmartOrderStatus.DRAFT) {
            throw new CustomException(AiErrorCode.SMART_ORDER_SUBMIT_FAILED);
        }
        this.smartOrderStatus = SmartOrderStatus.SUBMITTED;
    }




}
