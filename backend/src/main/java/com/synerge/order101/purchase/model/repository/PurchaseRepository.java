package com.synerge.order101.purchase.model.repository;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.purchase.model.dto.AutoPurchaseListResponseDto;
import com.synerge.order101.purchase.model.dto.PurchaseSummaryResponseDto;
import com.synerge.order101.purchase.model.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Query(
            value = """
            select new com.synerge.order101.purchase.model.dto.PurchaseSummaryResponseDto(
                p.purchaseId,
                su.supplierName,
                u.name,
                p.poNo,
                0,
                0,
                p.orderStatus,
                p.createdAt)
            from Purchase p
            left join p.supplier su
            left join p.user u
            where p.orderStatus = :status
            """
    )
    Page<PurchaseSummaryResponseDto> findByOrderStatus(OrderStatus status, Pageable pageable);

    @Query(
            value = """
            select new com.synerge.order101.purchase.model.dto.PurchaseSummaryResponseDto(
                p.purchaseId,
                su.supplierName,
                u.name,
                p.poNo,
                0,
                0,
                p.orderStatus,
                p.createdAt)
            from Purchase p
            left join p.supplier su
            left join p.user u
            """
    )
    Page<PurchaseSummaryResponseDto> findOrderAllStatus(Pageable pageable);

    @Query("""
        SELECT new com.synerge.order101.purchase.model.dto.AutoPurchaseListResponseDto(
            p.purchaseId,
            p.poNo,
            s.supplierName,
            CAST(COUNT(pd) AS int),
            CAST(COALESCE(SUM(pd.orderQty * pd.unitPrice), 0) AS int),
            p.createdAt,
            p.orderStatus
        )
        FROM Purchase p
        JOIN p.supplier s
        JOIN PurchaseDetail pd ON pd.purchase.purchaseId = p.purchaseId
        WHERE p.orderType = com.synerge.order101.purchase.model.entity.Purchase.OrderType.AUTO
    """)
    Page<AutoPurchaseListResponseDto> findAutoOrderAllStatus(Pageable pageable);

    @Query("""
        SELECT new com.synerge.order101.purchase.model.dto.AutoPurchaseListResponseDto(
            p.purchaseId,
            p.poNo,
            s.supplierName,
            CAST(COUNT(pd) AS int),
            CAST(COALESCE(SUM(pd.orderQty * pd.unitPrice), 0) AS int),
            p.createdAt,
            p.orderStatus
        )
        FROM Purchase p
        JOIN p.supplier s
        JOIN PurchaseDetail pd ON pd.purchase.purchaseId = p.purchaseId
        WHERE p.orderType = com.synerge.order101.purchase.model.entity.Purchase.OrderType.AUTO
          AND p.orderStatus = :status
    """)
    Page<AutoPurchaseListResponseDto> findByAutoOrderStatus(OrderStatus status, Pageable pageable);
}
