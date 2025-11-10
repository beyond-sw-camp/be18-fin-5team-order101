package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.warehouse.model.dto.request.InventoryQuantityChangeRequestDto;
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
}
