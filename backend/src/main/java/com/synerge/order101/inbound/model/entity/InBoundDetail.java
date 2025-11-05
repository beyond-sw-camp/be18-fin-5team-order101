package com.synerge.order101.inbound.model.entity;


import com.synerge.order101.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "inbound_detail")
public class InBoundDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inboundDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inbound_id", nullable = false)
    private InBound inboundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Product productId;

    @Column(name = "received_qty", nullable = false)
    private Integer receivedQuantity;

    @Column(columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
