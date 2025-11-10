package com.synerge.order101.product.controller;

import com.synerge.order101.common.dto.BaseResponseDto;
import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;
import com.synerge.order101.product.model.dto.ProductListRes;
import com.synerge.order101.product.model.dto.ProductRes;
import com.synerge.order101.product.model.dto.ProductUpdateReq;
import com.synerge.order101.product.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<BaseResponseDto<ProductCreateRes>> create(@RequestBody ProductCreateReq request) {
        ProductCreateRes productCreateRes = productService.create(request);
        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.CREATED, productCreateRes));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<BaseResponseDto<ProductRes>> update(@PathVariable Long productId,
                                                              @RequestBody ProductUpdateReq request) {
        ProductRes productRes = productService.update(productId, request);
        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, productRes));
    }

    @GetMapping
    public ResponseEntity<ItemsResponseDto<ProductListRes>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(required = false) String keyword
    ) {
        ItemsResponseDto<ProductListRes> body = productService.getProducts(page, numOfRows, keyword);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<BaseResponseDto<ProductRes>> getProduct(@PathVariable Long productId) {
        ProductRes dto = productService.getProduct(productId);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, dto));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<BaseResponseDto<String>> deleteProduct(@PathVariable Long productId) {

        productService.deleteProduct(productId);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, "상품이 삭제되었습니다."));
    }
}
