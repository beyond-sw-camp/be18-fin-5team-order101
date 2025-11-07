package com.synerge.order101.warehouse.controller;

import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.warehouse.model.dto.InventoryResponseDto;
import com.synerge.order101.warehouse.model.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouses/{warehouseId}")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

    // 창고 재고 조회
    @GetMapping("/inventory")
    public ResponseEntity<ItemsResponseDto<InventoryResponseDto>> getInventory(@RequestParam int page,
                                                                               @RequestParam int numOfRows,
                                                                               @PathVariable String warehouseId) {

        List<InventoryResponseDto> inventoryList = inventoryService.getInventoryList();
        int totalCount = inventoryList.size();

        return ResponseEntity.ok(new ItemsResponseDto<>(HttpStatus.OK, inventoryList, totalCount, page));
    }
}
