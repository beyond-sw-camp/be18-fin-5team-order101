package com.synerge.order101.product.model.service;

import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.common.exception.errorcode.CommonErrorCode;
import com.synerge.order101.product.exception.ProductErrorCode;
import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;
import com.synerge.order101.product.model.entity.CategoryLevel;
import com.synerge.order101.product.model.entity.ProductCategory;
import com.synerge.order101.product.model.repository.ProductCategoryRepository;
import com.synerge.order101.product.model.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            throw new CustomException(ProductErrorCode.INVALID_SMALL_CATEGORY);
        }

        ProductCategory medium = small.getParent();
        ProductCategory large = (medium != null) ? medium.getParent() : null;

    }
}
