package com.synerge.order101.purchase.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_detail_history")
public class PurchaseDetailHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseHistoryId;

    @Column(nullable = false)
    private Long purchaseOrderLineId;

    @Column(nullable = false)
    private Long purchaseId;

    @Column(nullable = false)
    private Long productId;

    private Integer beforeQty;

    private Integer afterQty;

    private Long changedBy;

    @Column(name = "updated_at", columnDefinition = "DATETIME")
    private LocalDateTime updatedAt;
}

