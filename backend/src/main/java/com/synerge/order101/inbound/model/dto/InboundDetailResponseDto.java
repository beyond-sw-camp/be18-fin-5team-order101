package com.synerge.order101.inbound.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class InboundDetailResponseDto {

    private String inboundNo; // INB-20230110-010

    private List<Item> items; // 화면 테이블에 뿌릴 리스트

    @Getter
    @Builder
    public static class Item {
        private String productCode;   // 제품 코드
        private String productName;   // 제품명
        private Integer receivedQty;  // 입고 수량
    }
}
