package com.synerge.order101.settlement.model.dto;

import com.synerge.order101.common.dto.BaseSearchCondition;
import com.synerge.order101.settlement.model.entity.Settlement;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스의 필드도
public class SettlementSearchCondition extends BaseSearchCondition {
    private String settlementType;
    private String settlementStatus;
    private String settlementNumber;

}
