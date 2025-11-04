package com.synerge.order101.order.model.entity;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "store_order_detatil")
public class StoreOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeOrderDetailId;

    @ManyToOne
    @JoinColumn(name = "store_order_id", nullable = false)
    private StoreOrder storeOrderId;

    @Column
    private Double orderQTY;

    @Column
    private Double unitPrice;


}