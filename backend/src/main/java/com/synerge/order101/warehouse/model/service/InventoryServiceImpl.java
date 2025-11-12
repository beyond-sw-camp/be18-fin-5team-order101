package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.warehouse.model.dto.response.InventoryResponseDto;
import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import com.synerge.order101.warehouse.model.repository.WarehouseInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final WarehouseInventoryRepository warehouseInventoryRepository;

    @Override
    @Transactional
    public List<InventoryResponseDto> getInventoryList() {

        return warehouseInventoryRepository.findAllWithProduct()
                .stream()
                .map(InventoryResponseDto::fromEntity)
                .toList();
    }

    // 출고 발생 시 재고 차감
    @Override
    @Transactional
    public void decreaseInventory(Long productId, int quantity) {
        WarehouseInventory inventory = warehouseInventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new IllegalStateException("해당 상품의 재고를 찾을 수 없습니다."));

        inventory.decrease(quantity);
    }

    // 입고 발생 시 재고 증가
    @Override
    @Transactional
    public void increaseInventory(Long productId, int quantity) {
        WarehouseInventory inventory = warehouseInventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new IllegalStateException("해당 상품의 재고를 찾을 수 없습니다."));

        inventory.increase(quantity);
    }
}
