package com.synerge.order101.product.model.service;

import com.synerge.order101.product.model.dto.ProductCreateReq;
import com.synerge.order101.product.model.dto.ProductCreateRes;

public interface ProductService {
    ProductCreateRes create(ProductCreateReq request);
}
