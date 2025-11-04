package com.synerge.order101.supplier.controller;

import com.synerge.order101.supplier.model.dto.SupplierListRes;
import com.synerge.order101.supplier.model.entity.Supplier;
import com.synerge.order101.supplier.model.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierListRes>> getAllSuppliers() {
        List<SupplierListRes> supplierListResDtos = supplierService.getSuppliers();
        return ResponseEntity.ok().body(new ArrayList<Supplier>());
    }
}
