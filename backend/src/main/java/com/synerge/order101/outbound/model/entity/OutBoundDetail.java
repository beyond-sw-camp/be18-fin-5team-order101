package com.synerge.order101.outbound.model.entity;


import com.synerge.order101.product.model.entity.Product;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "outbound_detail")
public class OutBoundDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboundDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "outbound_id", nullable = false)
    private OutBound outboundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Product productId;

    @Column(name = "outbound_qty", nullable = false)
    private Integer outboundQuantity;

    @Column(columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    private LocalDateTime createdAt;

}
