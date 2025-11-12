package com.synerge.order101.order.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.order.model.dto.*;
import java.util.List;

public interface StoreOrderService {
    public List<StoreOrderSummaryResponseDto> findOrders(OrderStatus status, Integer page, Integer size);

    public StoreOrderCreateResponseDto createOrder(StoreOrderCreateRequest request);

    public StoreOrderDetailResponseDto findStoreOrderDetails(Long storeOrderId);

    public StoreOrderUpdateStatusResponseDto updateOrderStatus(Long storeOrderId, OrderStatus newStatus);

}
