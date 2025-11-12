package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.warehouse.model.dto.request.InventoryQuantityChangeRequestDto;
import com.synerge.order101.warehouse.model.dto.response.InventoryResponseDto;

import java.util.List;

public interface InventoryService {
    List<InventoryResponseDto> getInventoryList();

}
