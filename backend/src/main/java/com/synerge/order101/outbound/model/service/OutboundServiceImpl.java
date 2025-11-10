package com.synerge.order101.outbound.model.service;

import com.synerge.order101.outbound.model.dto.OutboundDetailResponseDto;
import com.synerge.order101.outbound.model.dto.OutboundResponseDto;
import com.synerge.order101.outbound.model.entity.OutBound;
import com.synerge.order101.outbound.model.entity.OutboundDetail;
import com.synerge.order101.outbound.model.repository.OutboundDetailRepository;
import com.synerge.order101.outbound.model.repository.OutboundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService {
    private final OutboundRepository outboundRepository;
    private final OutboundDetailRepository outboundDetailRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OutboundResponseDto> getOutboundList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Object[]> result = outboundRepository.findOutboundWithCounts(pageable);

        return result.getContent().stream()
                .map(row -> {
                    OutBound o = (OutBound) row[0];
                    Integer itemCount = ((Number) row[1]).intValue();
                    Integer totalQty = ((Number) row[2]).intValue();

                    return new OutboundResponseDto(
                            o.getOutboundId(),
                            o.getOutboundNo(),
                            o.getOutboundDatetime(),
                            o.getStore().getStoreName(),
                            itemCount,
                            totalQty
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OutboundDetailResponseDto getOutboundDetail(Long outboundId) {
        OutBound outbound = outboundRepository.findById(outboundId)
                .orElseThrow(() -> new RuntimeException("출고 정보를 찾을 수 없습니다."));

        List<OutboundDetail> details = outboundDetailRepository.findByOutbound(outboundId);

        List<OutboundDetailResponseDto.Item> items = details.stream()
                .map(d -> OutboundDetailResponseDto.Item.builder()
                        .productCode(d.getProduct().getProductCode())
                        .productName(d.getProduct().getProductName())
                        .shippedQty(d.getOutboundQty())
                        .build())
                .toList();

        return OutboundDetailResponseDto.builder()
                .outboundNo(outbound.getOutboundNo())
                .items(items)
                .build();
    }
}
