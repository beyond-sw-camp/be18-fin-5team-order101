package com.synerge.order101.outbound.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OutboundDetailResponseDto {

    private String outboundNo; // OUT-20230110-010 (주석 예시 변경)

    private List<Item> items; // 화면 테이블에 뿌릴 리스트

    @Getter
    @Builder
    public static class Item {
        private String productCode;   // 제품 코드
        private String productName;   // 제품명
        private Integer shippedQty;  // 입고 수량 -> 출고 수량
    }
}
