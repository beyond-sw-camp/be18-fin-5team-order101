package com.synerge.order101.shipment.model.repository;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.dto.response.BasicShipmentRow;
import com.synerge.order101.shipment.model.dto.response.OrderQtyAgg;
import com.synerge.order101.shipment.model.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ShipmentListRepository extends JpaRepository<Shipment, Long> {

    // 1) 기본 목록 (집계 제외) — 쿼리 단순
    @Query("""
      select
        so.storeOrderId   as storeOrderId,
        so.orderNo        as orderNo,
        st.storeName      as storeName,
        wh.warehouseName  as warehouseName,
        sh.shipmentStatus as shipmentStatus,
        so.orderDatetime  as requestTime
      from StoreOrder so
        join so.store st
        join so.warehouse wh
        left join Shipment sh on sh.storeOrder = so
      where (:orderNo is null or so.orderNo like concat('%', :orderNo, '%'))
        and (:storeId is null or st.storeId = :storeId)
        and (:status  is null or sh.shipmentStatus = :status)
        and (:fromDt  is null or so.orderDatetime >= :fromDt)
        and (:toDt    is null or so.orderDatetime <= :toDt)
      order by so.orderDatetime desc
    """)
    Page<BasicShipmentRow> findBasicPage(
            @Param("orderNo") String orderNo,
            @Param("storeId") Long storeId,
            @Param("status") ShipmentStatus status,
            @Param("fromDt") LocalDateTime from,
            @Param("toDt") LocalDateTime to,
            Pageable pageable
    );

    // 2) 합계 한 방 집계 (N+1 방지) — IN (...) GROUP BY
    @Query("""
      select
        sod.storeOrder.storeOrderId as storeOrderId,
        coalesce(sum(sod.orderQty), 0) as totalQty
      from StoreOrderDetail sod
      where sod.storeOrder.storeOrderId in :orderIds
      group by sod.storeOrder.storeOrderId
    """)
    List<OrderQtyAgg> sumOrderQtyByOrderIds(@Param("orderIds") Collection<Long> orderIds);
}
