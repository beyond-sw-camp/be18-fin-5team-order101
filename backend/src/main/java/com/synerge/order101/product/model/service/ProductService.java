package com.synerge.order101.product.model.service;

import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;
import com.synerge.order101.product.model.dto.ProductInventoryDetailRes;
import com.synerge.order101.product.model.dto.ProductListRes;
import com.synerge.order101.product.model.dto.ProductRes;
import com.synerge.order101.product.model.dto.ProductUpdateReq;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductCreateRes create(ProductCreateReq request, MultipartFile imageFile);

    ItemsResponseDto<ProductListRes> getProducts(int page, int numOfRows, String keyword,
                                                 Long largeCategoryId, Long mediumCategoryId, Long smallCategoryId);

    void deleteProduct(Long productId);

    ProductRes getProduct(Long productId);

    ProductRes update(Long productId, ProductUpdateReq request, MultipartFile imageFile);

    ProductInventoryDetailRes getProductInventory(Long productId, int page, int numOfRows);
}
