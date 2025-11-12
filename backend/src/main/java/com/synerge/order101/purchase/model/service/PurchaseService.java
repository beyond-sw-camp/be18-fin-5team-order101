package com.synerge.order101.purchase.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.purchase.model.dto.PurchaseCreateRequest;
import com.synerge.order101.purchase.model.dto.PurchaseDetailResponseDto;
import com.synerge.order101.purchase.model.dto.PurchaseSummaryResponseDto;
import com.synerge.order101.purchase.model.dto.PurchaseUpdateStatusResponseDto;

import java.util.List;

public interface PurchaseService {


    // 발주 목록 조회
    List<PurchaseSummaryResponseDto> findPurchases(OrderStatus status, Integer page, Integer size);

    // 발주 상세 조회
    PurchaseDetailResponseDto findPurchaseDetailsById(Long purchaseOrderId);

    // 발주 생성
    void createPurchase(PurchaseCreateRequest purchaseCreateRequest);

    // 발주 상태 업데이트
    PurchaseUpdateStatusResponseDto updatePurchaseStatus(Long purchaseOrderId, OrderStatus status);
}

