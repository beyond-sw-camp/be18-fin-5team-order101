package com.synerge.order101.product.controller;

import com.synerge.order101.product.model.entity.CategoryLevel;
import com.synerge.order101.product.model.entity.ProductCategory;
import com.synerge.order101.product.model.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ProductCategoryRepository productCategoryRepository;

    @GetMapping("/top")
    public List<CategorySimpleDto> top(){
        return productCategoryRepository.findByCategoryLevelAndParentIsNull(CategoryLevel.LARGE)
                .stream().map(CategorySimpleDto::from).toList();
    }

    @GetMapping("/{parentId}/children")
    public List<CategorySimpleDto> children(@PathVariable Long parentId){
        return productCategoryRepository.findByParent_ProductCategoryId(parentId)
                .stream().map(CategorySimpleDto::from).toList();
    }
}

record CategorySimpleDto(Long id, String name) {
    static CategorySimpleDto from(ProductCategory c) { return new CategorySimpleDto(c.getProductCategoryId(), c.getCategoryName()); }
}
