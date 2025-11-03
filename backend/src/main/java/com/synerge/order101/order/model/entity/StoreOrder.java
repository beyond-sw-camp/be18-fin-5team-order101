package com.synerge.order101.order.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "store_order")
@NoArgsConstructor
public class StoreOrder {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long storeOrderId;

//    Long orderId;
//
//    Long storeId;
//
//    Long warehouseId;

    @Column
    String order_no;

    @Column
    OffsetDateTime order_datetime;

    @Enumerated(EnumType.STRING) // Enum 타입을 데이터베이스에 저장할 때 문자열로 저장하도록 설정
    DeliveryStatus delivery_status;

    @Column
    String order_status;

    @Column
    String remark;

    @Column
    @CreatedDate
    OffsetDateTime createdAt;

    @Column
    @LastModifiedDate
    OffsetDateTime updatedAt;

    public StoreOrder(StoreOrder storeOrderDto) {
        this.storeOrderId = storeOrderDto.getStoreOrderId();
    }

}
