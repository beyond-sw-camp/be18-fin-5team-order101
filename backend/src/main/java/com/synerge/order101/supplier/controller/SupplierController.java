package com.synerge.order101.supplier.controller;

import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.supplier.model.dto.SupplierListRes;
import com.synerge.order101.supplier.model.service.SupplierServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierServiceImpl supplierService;
    
    @GetMapping("/suppliers")
    public ResponseEntity<ItemsResponseDto<SupplierListRes>> getAllSuppliers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(required = false) String keyword
    ) {
        ItemsResponseDto<SupplierListRes> body = supplierService.getSuppliers(page, numOfRows, keyword);
        return ResponseEntity.ok(body);
    }
}
