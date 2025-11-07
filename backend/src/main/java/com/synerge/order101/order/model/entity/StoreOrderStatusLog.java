package com.synerge.order101.order.model.entity;

import com.synerge.order101.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.jmx.export.annotation.ManagedAttribute;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store_order_status_log")
public class StoreOrderStatusLog {

    @Id
    @Column(name = "store_order_status_log_id")
    private String storeOrderStatusLogId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name="store_order_id")
    private StoreOrder storeOrder;

    @Column(name = "updated_at")
    private LocalDateTime updated_at;

    @Column(name = "prev_order_status")
    private OrderStatus prevStatus;

    @Column(name = "cur_order_status")
    private OrderStatus curStatus;

}

