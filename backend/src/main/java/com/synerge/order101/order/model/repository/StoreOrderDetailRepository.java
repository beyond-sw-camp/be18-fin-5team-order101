package com.synerge.order101.order.model.repository;

import com.synerge.order101.order.model.entity.StoreOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoreOrderDetailRepository extends JpaRepository<StoreOrderDetail,Long> {

    List<StoreOrderDetail> findByStoreOrder_StoreOrderId(Long storeOrderId);

    @Query("""
        SELECT SUM(o.orderQty)
        FROM StoreOrderDetail o
        WHERE o.product.productId = :productId
          AND o.storeOrder.orderStatus = 'CONFIRMED'
          AND o.storeOrder.updatedAt >= :fromDate
        GROUP BY FUNCTION('date', o.storeOrder.updatedAt)
        ORDER BY FUNCTION('date', o.storeOrder.updatedAt)
    """)
    List<Integer> findDailySalesQtySince(@Param("productId") Long productId,
                                        @Param("fromDate") LocalDateTime fromDate
    );

}
