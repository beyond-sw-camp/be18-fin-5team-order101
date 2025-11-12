package com.synerge.order101.order.model.dto;

import com.synerge.order101.common.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.Internal;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StoreOrderSummaryResponseDto {

    private Long storeOrderId;

    private String orderNo;

    private String storeName;

    // 품목 수
    private Integer itemCount;

    // 총 수량
    private Integer totalQTY;

    private LocalDateTime orderDate;

    private OrderStatus orderStatus;

}
