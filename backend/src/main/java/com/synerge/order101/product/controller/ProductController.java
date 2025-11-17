package com.synerge.order101.product.controller;

import com.synerge.order101.common.dto.BaseResponseDto;
import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;
import com.synerge.order101.product.model.dto.ProductInventoryDetailRes;
import com.synerge.order101.product.model.dto.ProductListRes;
import com.synerge.order101.product.model.dto.ProductRes;
import com.synerge.order101.product.model.dto.ProductUpdateReq;
import com.synerge.order101.product.model.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDto<ProductCreateRes>> create(@RequestPart("request") ProductCreateReq request,
                                                                    @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        ProductCreateRes productCreateRes = productService.create(request, imageFile);
        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.CREATED, productCreateRes));
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponseDto<ProductRes>> update(@PathVariable Long productId,
                                                              @RequestPart("request") ProductUpdateReq request,
                                                              @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        ProductRes productRes = productService.update(productId, request, imageFile);
        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, productRes));
    }

    @GetMapping
    public ResponseEntity<ItemsResponseDto<ProductListRes>> getAllProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long largeCategoryId,
            @RequestParam(required = false) Long mediumCategoryId,
            @RequestParam(required = false) Long smallCategoryId
    ) {
        ItemsResponseDto<ProductListRes> body = productService.getProducts(page, numOfRows, keyword,
                largeCategoryId, mediumCategoryId, smallCategoryId);
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

    @GetMapping("/{productId}/inventory")
    public ResponseEntity<BaseResponseDto<ProductInventoryDetailRes>> getInventory(@PathVariable Long productId,
                                                                                   @RequestParam(defaultValue = "1") int page,
                                                                                   @RequestParam(defaultValue = "10") int numOfRows) {
        var res = productService.getProductInventory(productId, page, numOfRows);
        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, res));
    }
}
