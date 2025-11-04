package com.synerge.order101.order.model.entity;

import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.user.model.entity.User;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "store_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StoreOrder {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long storeOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    Warehouse warehouse;

    @Column
    String orderNo;

    @Column
    OffsetDateTime orderDatetime;

    @Enumerated(EnumType.STRING) // Enum 타입을 데이터베이스에 저장할 때 문자열로 저장하도록 설정
    DeliveryStatus deliveryStatus;

    @Column
    OrderStatus orderStatus;

    @Column
    String remark;

    @Column
    @CreatedDate
    OffsetDateTime createdAt;

    @Column
    @LastModifiedDate
    OffsetDateTime updatedAt;

    public enum OrderStatus{
        SUBMITTED,CONFIRMED
    }


}
