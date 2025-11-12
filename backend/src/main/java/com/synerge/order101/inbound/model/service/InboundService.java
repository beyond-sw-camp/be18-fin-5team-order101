package com.synerge.order101.inbound.model.service;

import com.synerge.order101.inbound.model.dto.InboundDetailResponseDto;
import com.synerge.order101.inbound.model.dto.InboundResponseDto;

import java.util.List;

public interface InboundService {
    List<InboundResponseDto> getInboundList(int page, int size);

    InboundDetailResponseDto getInboundDetail(Long inboundId);
}
