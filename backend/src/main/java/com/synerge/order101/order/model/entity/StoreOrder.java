//package com.synerge.order101.order.model.entity;
//
//import com.synerge.order101.common.enums.OrderStatus;
//import com.synerge.order101.common.enums.ShipmentStatus;
//import com.synerge.order101.store.model.entity.Store;
//import com.synerge.order101.user.model.entity.User;
//import com.synerge.order101.warehouse.model.entity.Warehouse;
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import java.time.OffsetDateTime;
//
//@Getter
//@Entity
//@EntityListeners(AuditingEntityListener.class)
//@Table(name = "store_order")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
//@Builder
//public class StoreOrder {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    Long storeOrderId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "store_id", nullable = false)
//    Store store;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    User user;
//
//    @ManyToOne
//    @JoinColumn(name = "warehouse_id", nullable = false)
//    Warehouse warehouse;
//
//    @Column
//    String orderNo;
//
//    @Column
//    OffsetDateTime orderDatetime;
//
//    @Enumerated(EnumType.STRING) // Enum 타입을 데이터베이스에 저장할 때 문자열로 저장하도록 설정
//    ShipmentStatus shipmentStatus;
//
//    @Column
//    OrderStatus orderStatus;
//
//    @Column
//    String remark;
//
//    @Column
//    @CreatedDate
//    OffsetDateTime createdAt;
//
//    @Column
//    @LastModifiedDate
//    OffsetDateTime updatedAt;
//
//
//}


package com.synerge.order101.order.model.entity;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.store.model.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "store_order")
public class StoreOrder {

    @Id
    @Column(name = "store_order_id")
    private Long storeOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Store store;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    @Column(name = "order_datetime")
    private LocalDateTime orderDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status", nullable = false, length = 20)
    private ShipmentStatus shipmentStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 20)
    private OrderStatus orderStatus;

    @Column(name = "remark")
    private String remark;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

