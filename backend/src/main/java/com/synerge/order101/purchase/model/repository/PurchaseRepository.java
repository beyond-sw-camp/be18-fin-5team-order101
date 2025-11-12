package com.synerge.order101.purchase.model.repository;

import com.synerge.order101.common.enums.OrderStatus;
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
}
