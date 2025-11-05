package com.synerge.order101.product.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateReq {
    private Long productName;
    private String productCode;
    private BigDecimal price;
    private Boolean status;
    private String description;
    private String imageUrl;

    Long categorySmallId;      // 소분류 id (leaf)
    Long categoryMediumId;     // (검증용/선택적으로 받고 parent 체인 확인)
    Long categoryLargeId;
}
