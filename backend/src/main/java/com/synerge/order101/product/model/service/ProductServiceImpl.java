package com.synerge.order101.product.model.service;

import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.product.exception.ProductErrorCode;
import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;
import com.synerge.order101.product.model.dto.ProductListRes;
import com.synerge.order101.product.model.dto.ProductRes;
import com.synerge.order101.product.model.dto.ProductUpdateReq;
import com.synerge.order101.product.model.entity.CategoryLevel;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.product.model.entity.ProductCategory;
import com.synerge.order101.product.model.repository.ProductCategoryRepository;
import com.synerge.order101.product.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    @Override
    public ProductCreateRes create(ProductCreateReq request) {

        ProductCategory small = productCategoryRepository.findById(request.getCategorySmallId()).orElseThrow(() ->
                new CustomException(ProductErrorCode.INVALID_SMALL_CATEGORY));

        if(small.getCategoryLevel() != CategoryLevel.SMALL) {
            throw new CustomException(ProductErrorCode.MUST_BE_SMALL);
        }

        ProductCategory medium = small.getParent();
        ProductCategory large = (medium != null) ? medium.getParent() : null;

        if(request.getCategoryMediumId() != null && (medium == null || !medium.getProductCategoryId().equals(request.getCategoryMediumId()))) {
            throw new CustomException(ProductErrorCode.SMALL_MEDIUM);
        }
        if(request.getCategoryLargeId() != null && (large == null || !large.getProductCategoryId().equals(request.getCategoryLargeId()))) {
            throw new CustomException(ProductErrorCode.LARGE_MEDIUM);
        }

        Product product = Product.builder()
                .productCode(request.getProductCode())
                .productName(request.getProductName())
                .status(Boolean.TRUE.equals(request.getStatus()))
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .productCategory(small)
                .build();

        productRepository.save(product);
        return new  ProductCreateRes(product.getProductId());
    }

    @Override
    public ProductRes update(Long productId, ProductUpdateReq request) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        ProductCategory small = productCategoryRepository.findById(request.getCategorySmallId()).orElseThrow(() ->
                new CustomException(ProductErrorCode.INVALID_SMALL_CATEGORY));

        if(small.getCategoryLevel() != CategoryLevel.SMALL) {
            throw new CustomException(ProductErrorCode.MUST_BE_SMALL);
        }

        ProductCategory medium = small.getParent();
        ProductCategory large = (medium != null) ? medium.getParent() : null;

        if(request.getCategoryMediumId() != null && (medium == null || !medium.getProductCategoryId().equals(request.getCategoryMediumId()))) {
            throw new CustomException(ProductErrorCode.SMALL_MEDIUM);
        }
        if(request.getCategoryLargeId() != null && (large == null || !large.getProductCategoryId().equals(request.getCategoryLargeId()))) {
            throw new CustomException(ProductErrorCode.LARGE_MEDIUM);
        }

        product.update(
                request.getProductCode(),
                request.getProductName(),
                request.getDescription(),
                request.getPrice(),
                request.getImageUrl(),
                request.getStatus(),
                small
        );

        return ProductRes.builder()
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .categorySmallName(small.getCategoryName())
                .categoryMediumName(medium != null ? medium.getCategoryName() : null)
                .categoryLargeName(large !=  null ? large.getCategoryName() : null)
                .price(request.getPrice())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .build();

    }

    @Override
    public ItemsResponseDto<ProductListRes> getProducts(int page, int numOfRows, String keyword) {
        int pageIndex = Math.max(0, page - 1);

        Pageable pageable = PageRequest.of(pageIndex, numOfRows, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> productPage;
        if(keyword != null && !keyword.isBlank()) {
            productPage = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }

        List<ProductListRes> items = productPage.getContent().stream()
                .map(s -> ProductListRes.builder()
                        .productId(s.getProductId())
                        .productCode(s.getProductCode())
                        .categoryId(s.getProductCategory().getProductCategoryId())
                        .categoryName(s.getProductCategory().getCategoryName())
                        .categoryLevel(String.valueOf(s.getProductCategory().getCategoryLevel()))
                        .productName(s.getProductName())
                        .price(s.getPrice())
                        .status(s.getStatus())
                        .build())
                .toList();
        int totalCount = (int) productPage.getTotalElements();

        return new ItemsResponseDto<>(HttpStatus.OK, items, page, totalCount);
    }

    @Override
    public ProductRes getProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        return ProductRes.builder()
                .productName(product.getProductName())
                .productCode(product.getProductCode())
                .categoryLargeName(product.getProductCategory().getParent().getParent().getCategoryName())
                .categoryMediumName(product.getProductCategory().getParent().getCategoryName())
                .categorySmallName(product.getProductCategory().getCategoryName())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .description(product.getDescription())
                .build();
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(() ->
                new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));
        // 나중에 유저 권한 검증 구현
        // cascade, orphan
        productRepository.delete(product);
    }


}
