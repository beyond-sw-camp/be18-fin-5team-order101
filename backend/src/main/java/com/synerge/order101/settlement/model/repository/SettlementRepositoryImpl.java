package com.synerge.order101.settlement.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.synerge.order101.common.enums.SettlementType;
import com.synerge.order101.settlement.model.dto.SettlementSearchCondition;
import com.synerge.order101.settlement.model.entity.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import java.util.List;

import static com.synerge.order101.settlement.model.entity.QSettlement.settlement;

@RequiredArgsConstructor
@Repository
public class SettlementRepositoryImpl implements SettlementRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Settlement> search(SettlementSearchCondition cond, Pageable pageable) {
        //데이터 목록 조회
        List<Settlement> content = queryFactory
                .selectFrom(settlement)
                .where(
                        statusEq(cond.getSettlementStatus()),
                        typeEq(cond.getSettlementType()),
                        numberEq(cond.getSettlementNumber())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(settlement.createdAt.desc())
                .fetch();
        // 전체 카운트 조회
        Long total = queryFactory
                .select(settlement.count())
                .from(settlement)
                .where(
                        statusEq(cond.getSettlementStatus()),
                        typeEq(cond.getSettlementType()),
                        numberEq(cond.getSettlementNumber())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    // --- 동적 쿼리 조건을 생성하는 BooleanExpression 메소드들 ---}
    private BooleanExpression statusEq(String status) {
        if(!StringUtils.hasText(status)){
            return null;
        }
        try {
            Settlement.SettlementStatus settlementStatus = Settlement.SettlementStatus.valueOf(status.toUpperCase());
            return settlement.settlementStatus.eq(settlementStatus);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private  BooleanExpression typeEq(String type) {
        if(!StringUtils.hasText(type)){
            return null;
        }
        try {
            SettlementType settlementType = SettlementType.valueOf(type.toUpperCase());
            return settlement.settlementType.eq(settlementType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private BooleanExpression numberEq(String number) {
        if(!StringUtils.hasText(number)){
            return null;
        }
        try{
            return settlement.settlementId.eq(Long.parseLong(number));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
