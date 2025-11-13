package com.synerge.order101.settlement.model.entity;

import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.supplier.model.entity.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "settlement")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settlementId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String settlementNo;

    @Column(nullable = false)
    private SettlementType settlementType;

    @Column(nullable = false)
    private SettlementStatus settlementStatus;

    @Column
    private String note;

    // 정산 완료 날짜
    @Column(nullable = false)
    private LocalDateTime settledDate;

    // 정산에 필요한 금액
    @Column(nullable = false)
    private BigDecimal amountGoods;

    // 정산 요청 시각
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.settlementNo = generateSettlementNo();
    }

    public String generateSettlementNo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePart = LocalDateTime.now().format(formatter);
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "SETL-" + datePart + randomNum;
    }

    public enum SettlementType {
        AR, //가맹점 청구
        AP // 공급사 지급
    }

    public enum SettlementStatus {
        DRAFT, //초안
        ISSUED, //발행됨
        VOID, //확정
    }


}
