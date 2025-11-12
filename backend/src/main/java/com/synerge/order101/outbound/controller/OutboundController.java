package com.synerge.order101.outbound.controller;

import com.synerge.order101.common.dto.BaseResponseDto;
import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.outbound.model.dto.OutboundDetailResponseDto;
import com.synerge.order101.outbound.model.dto.OutboundResponseDto;
import com.synerge.order101.outbound.model.service.OutboundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/outbounds")
@RequiredArgsConstructor
public class OutboundController {
    private final OutboundService outboundService;

    // 전체 출고 조회
    @GetMapping
    public ResponseEntity<ItemsResponseDto<OutboundResponseDto>> getOutbounds(@RequestParam(defaultValue = "0") int page,
                                                                              @RequestParam(defaultValue = "20") int size) {

        List<OutboundResponseDto> outboundList = outboundService.getOutboundList(page, size);
        int totalCount = outboundList.size(); // 참고: 실제 총 개수를 서비스에서 받아오는 것이 더 정확합니다.

        return ResponseEntity.ok(new ItemsResponseDto<>(HttpStatus.OK, outboundList, totalCount, page));
    }

    // 출고 상세 조회
    @GetMapping("/{outboundId}")
    public ResponseEntity<BaseResponseDto<OutboundDetailResponseDto>> getOutboundDetail(@PathVariable Long outboundId) {

        OutboundDetailResponseDto outboundDetail = outboundService.getOutboundDetail(outboundId);

        return ResponseEntity.ok(new BaseResponseDto<>(HttpStatus.OK, outboundDetail));
    }
}
