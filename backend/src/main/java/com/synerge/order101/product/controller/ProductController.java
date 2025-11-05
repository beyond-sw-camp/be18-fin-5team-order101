package com.synerge.order101.product.controller;

import com.synerge.order101.common.dto.BaseResponseDto;
import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;
import com.synerge.order101.product.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<BaseResponseDto<ProductCreateRes>> create(@RequestBody ProductCreateReq request) {
        ProductCreateRes productCreateRes = productService.create(request);
        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.CREATED, productCreateRes));
    }
}
