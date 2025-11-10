package com.synerge.order101.outbound.model.service;

import com.synerge.order101.outbound.model.dto.OutboundDetailResponseDto;
import com.synerge.order101.outbound.model.dto.OutboundResponseDto;

import java.util.List;

public interface OutboundService {
    List<OutboundResponseDto> getOutboundList(int page, int size);

    OutboundDetailResponseDto getOutboundDetail(Long outboundId);
}
