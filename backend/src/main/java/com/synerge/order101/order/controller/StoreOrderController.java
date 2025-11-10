package com.synerge.order101.order.controller;

import com.synerge.order101.order.model.dto.*;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.order.model.service.StoreOrderService;
import com.synerge.order101.order.model.service.StoreOrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // ResponseBody를 선언할 필요가 없어 코드가 간결해진다.
@RequestMapping("/api/v1/store-orders")
@RequiredArgsConstructor
public class StoreOrderController {

    private final StoreOrderService storeOrderService;

    @GetMapping
    public ResponseEntity<List<StoreOrderSummaryResponseDto>> findStoreOrders(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page
    ) {
        List<StoreOrderSummaryResponseDto> response = storeOrderService.findOrders(status, page);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllStoreOrders() {

        return ResponseEntity.noContent().build();
    }

    /**
     * 2. 가맹점 주문 생성 (POST /orders)
     * - 요청 본문(RequestBody)에서 주문 정보를 받아 새로운 주문을 생성합니다.
     */
    @PostMapping
    public ResponseEntity<StoreOrderCreateResponseDto> createStoreOrder(@RequestBody StoreOrderCreateRequest request) {

        StoreOrderCreateResponseDto responseDto = storeOrderService.createOrder(request);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 3. 주문 상세 조회 (GET /orders/{orderId})
     * - 경로 변수(PathVariable)로 받은 주문 ID에 해당하는 상세 정보를 반환합니다.
     */
    @GetMapping("/{storeOrderId}")
    public ResponseEntity<StoreOrderDetailResponseDto> findStoreOrderById(@PathVariable Long storeOrderId) {

        StoreOrderDetailResponseDto response = storeOrderService.findStoreOrderDetails(storeOrderId);

        return ResponseEntity.ok(response);
    }

    /**
     * 4. 주문 승인 (PUT /orders/{orderId}/approve)
     * - 특정 주문의 상태를 '승인'으로 변경합니다.
     */
    @PatchMapping("/{storeOrderId}/approve")
    public ResponseEntity<StoreOrderUpdateStatusResponseDto> approveStoreOrder(@PathVariable Long storeOrderId) {

        StoreOrderUpdateStatusResponseDto responseDto = storeOrderService.approveOrder(storeOrderId);

        return ResponseEntity.ok(responseDto);
    }

    /**
     * 5. 주문 반려 (PUT /orders/{orderId}/reject)
     * - 특정 주문의 상태를 '반려/취소'로 변경합니다.
     */
    @PatchMapping("/{storeOrderId}/reject")
    public ResponseEntity<StoreOrderUpdateStatusResponseDto> rejectOrder(@PathVariable Long storeOrderId) {

        StoreOrderUpdateStatusResponseDto responseDto = storeOrderService.rejectOrder(storeOrderId);

        return ResponseEntity.ok(responseDto);
    }
}
