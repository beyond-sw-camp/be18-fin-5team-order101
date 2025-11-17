package com.synerge.order101.common.enums;

public enum OrderStatus {
    DRAFT_AUTO,        // 자동 생성 초안
    SUBMITTED,         // 담당자 확정 및 제출
    CONFIRMED,         // 관리자 승인
    REJECTED,          // 관리자 반려
    CANCELLED          // 담당자 취소
}
