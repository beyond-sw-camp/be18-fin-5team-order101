package com.synerge.order101.warehouse.controller;

import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.warehouse.model.dto.request.InventoryQuantityChangeRequestDto;
import com.synerge.order101.warehouse.model.dto.response.InventoryResponseDto;
import com.synerge.order101.warehouse.model.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    // 창고 재고 조회
    @GetMapping("/inventory")
    public ResponseEntity<ItemsResponseDto<InventoryResponseDto>> getInventory(@RequestParam int page,
                                                                               @RequestParam int numOfRows) {

        List<InventoryResponseDto> inventoryList = inventoryService.getInventoryList();
        int totalCount = inventoryList.size();

        return ResponseEntity.ok(new ItemsResponseDto<>(HttpStatus.OK, inventoryList, totalCount, page));
    }

//    // 입고 수량 반영
//    @PatchMapping("/inbound")
//    public ResponseEntity<Void> inbound(@RequestBody InventoryQuantityChangeRequestDto request) {
//        inventoryService.inbound(request);
//        return ResponseEntity.ok().build();
//    }
//
//    // 출고 수량 반영
//    @PatchMapping("/outbound")
//    public ResponseEntity<Void> outbound(@RequestBody InventoryQuantityChangeRequestDto request) {
//        inventoryService.outbound(request);
//        return ResponseEntity.ok().build();
//    }
}
