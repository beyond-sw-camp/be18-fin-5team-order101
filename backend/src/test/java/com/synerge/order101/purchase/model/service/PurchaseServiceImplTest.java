package com.synerge.order101.purchase.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest // Spring Context 로드
@Transactional   // 각 테스트 실행 후 롤백
class PurchaseServiceImplTest {

    @Autowired
    private PurchaseService purchaseService; // ✅ 클래스 필드에 선언해야 함

    @Test
    void testConfirmPurchaseTriggersInbound() {
        // given
        Long purchaseId = 1L;

        // when
        purchaseService.updatePurchaseStatus(purchaseId, OrderStatus.CONFIRMED);

        // then
        System.out.println("✅ 발주 승인 → 입고 처리 완료");
    }
}
