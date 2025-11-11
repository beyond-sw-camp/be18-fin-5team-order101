package com.synerge.order101.purchase.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.order.model.dto.StoreOrderSummaryResponseDto;
import com.synerge.order101.purchase.model.dto.PurchaseCreateRequest;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PurchaseService {

    void createPurchase(PurchaseCreateRequest purchaseCreateRequest);

    // 발주 목록 조회

    // 발주 상세 조회

    // 발주 생성

    // 공급사 승인 처리
}
