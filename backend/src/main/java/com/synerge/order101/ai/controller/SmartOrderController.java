package com.synerge.order101.ai.controller;


import com.synerge.order101.ai.model.dto.request.SmartOrderUpdateRequest;
import com.synerge.order101.ai.model.dto.response.SmartOrderResponseDto;
import com.synerge.order101.ai.model.entity.SmartOrder;
import com.synerge.order101.ai.service.SmartOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/smart-orders")
@RequiredArgsConstructor
public class SmartOrderController {
    private final SmartOrderService smartOrderService;

    //스마트 발주 초안 작성(AI)
    @PostMapping("/generate")
    public ResponseEntity<List<SmartOrderResponseDto>> generateSmartOrders(
            @RequestParam("targetWeek")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate targetWeek
    ){
        return ResponseEntity.ok(smartOrderService.generateSmartOrders(targetWeek));
    }

    //스마트 발주 목록 조회
    @GetMapping
    public ResponseEntity<List<SmartOrderResponseDto>> getSmartOrders(
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "status", required = false) SmartOrder.SmartOrderStatus status
    ) {
        return ResponseEntity.ok(smartOrderService.getSmartOrders(storeId, status));
    }

    //스마트 발주 상세 조회
    @GetMapping("/{SmartOrderId}")
    public ResponseEntity<SmartOrderResponseDto> getSmartOrder(
            @PathVariable Long smartOrderId
    ){
        return ResponseEntity.ok(smartOrderService.getSmartOrder(smartOrderId));
    }

    //스마트 발주 수정
    @PatchMapping("/{SmartOrderId}")
    public ResponseEntity<SmartOrderResponseDto> updateDraft(
            @PathVariable Long smartOrderId,
            @RequestBody SmartOrderUpdateRequest request
    ) {
        return ResponseEntity.ok(smartOrderService.updateDraft(smartOrderId, request));
    }

    //직원이 제출
    //이거는 필요 없을 수도
    @PostMapping("/{smartOrderId}/submit")
    public ResponseEntity<SmartOrderResponseDto> submit(
            @PathVariable Long smartOrderId
    ) {
        return ResponseEntity.ok(smartOrderService.submit(smartOrderId));
    }
}
