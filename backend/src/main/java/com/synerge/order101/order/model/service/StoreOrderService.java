package com.synerge.order101.order.model.service;

import com.synerge.order101.order.model.dto.*;
import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;

import java.util.Collections;
import java.util.List;

public interface StoreOrderService {
    public List<StoreOrderSummaryResponseDto> findOrders(String status, Integer page);

    public StoreOrderCreateResponseDto createOrder(StoreOrderCreateRequest request);

    public StoreOrderDetailResponseDto findStoreOrderDetails(Long storeOrderId);

    public StoreOrderUpdateStatusResponseDto approveOrder(Long storeOrderId);

    public StoreOrderUpdateStatusResponseDto rejectOrder(Long storeOrderId);
}
