package com.synerge.order101.order.model.entity;
public enum DeliveryStatus {

    READY_FOR_SHIPMENT("배송 준비"),
    IN_TRANSIT("배송 중"),
    DELIVERED("배송 완료");

    private final String koreanName;

    DeliveryStatus(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }
}
// 사용 예: DeliveryStatus.READY_FOR_SHIPMENT.getKoreanName() -> "배송 준비"