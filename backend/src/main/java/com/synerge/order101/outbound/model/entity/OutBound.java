package com.synerge.order101.outbound.model.entity;


import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "outbound")
public class OutBound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboundId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store storeId;

    @Column(nullable = false)
    private String outboundNo;

    @Column(name = "outbound_datetime", columnDefinition = "DATETIME(6)")
    @CreationTimestamp
    private LocalDateTime outboundDatetime;

    private String createdBy;

}
