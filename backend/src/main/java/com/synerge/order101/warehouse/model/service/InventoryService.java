package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.warehouse.model.dto.InventoryResponseDto;

import java.util.List;

public interface InventoryService {
    List<InventoryResponseDto> getInventoryList();
}
