package com.synerge.order101.settlement.model.dto;

import com.synerge.order101.common.dto.BaseSearchCondition;
import com.synerge.order101.settlement.model.entity.Settlement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스의 필드도
public class SettlementSearchCondition extends BaseSearchCondition {
    // 정산 유형: 체크된 값들(AR, AP)을 리스트로 받습니다.
    private List<String> types;

    // 상태: 체크된 값들(DRAFT, ISSUED, VOID)을 리스트로 받습니다.
    private List<String> statuses;

    // 3. 기간 (From, To 필드를 명확히 나누는 것이 좋습니다.)
    private String period; // Vue에서 YYYY-MM 형식으로 오면 이를 처리합니다.

    // 4. 검색어
    private String searchText; // ID 또는 공급사 이름 검색

}
