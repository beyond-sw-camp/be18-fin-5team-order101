package com.synerge.order101.purchase.controller;

import com.synerge.order101.purchase.model.dto.PurchaseCreateRequest;
import com.synerge.order101.purchase.model.entity.Purchase;
import com.synerge.order101.purchase.model.entity.PurchaseDetail;
import com.synerge.order101.purchase.model.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    /*
    1. 발주 목록 조회
    - 모든 발주 목록을 조회한다.
    */

    @GetMapping
    public ResponseEntity<List<Purchase>> getPurchases() {

        return null;
    }

    /*
    2. 발주 상세 조회
    - 특정 발주의 상세 정보를 조회한다.
    */
    @GetMapping("/{purchaseOrderId}")
    public ResponseEntity<Purchase> getPurchase(@RequestParam long purchaseOrderId) {

        return null;
    }

    /*
    3. 발주 생성
    - 새로운 발주를 생성한다.
     */
    @PostMapping
    public ResponseEntity<PurchaseDetail> createPurchase(@RequestBody PurchaseCreateRequest purchaseCreateRequest) {

        purchaseService.createPurchase(purchaseCreateRequest);

        return null;
    }

    /*
    4. 공급사 승인 처리
    - 특정 발주에 대해 공급사의 승인을 처리한다.
     */
    @PatchMapping
    public ResponseEntity<Purchase> approvePurchase(@RequestParam long id) {
        return null;
    }
}

