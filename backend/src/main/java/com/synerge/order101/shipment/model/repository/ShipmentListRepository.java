package com.synerge.order101.shipment.model.repository;

import com.synerge.order101.common.enums.ShipmentStatus;
import com.synerge.order101.shipment.model.dto.response.ShipmentResponseDto;
import com.synerge.order101.shipment.model.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ShipmentListRepository extends JpaRepository<Shipment, Long> {

    @Query(value = """
        select new com.synerge.order101.shipment.model.dto.response.ShipmentResponseDto(
            so.storeOrderId,
            so.orderNo,
            st.storeName,
            cast(coalesce(sum(sod.orderQty), 0) as bigdecimal),
            sh.shipmentStatus,
            so.orderDatetime
        )
        from StoreOrder so
            join so.store st
            left join Shipment sh on sh.storeOrder.storeOrderId = so.storeOrderId
            left join StoreOrderDetail sod on sod.storeOrder.storeOrderId = so.storeOrderId
        where (:orderNo is null or so.orderNo like concat('%', :orderNo, '%'))
          and (:storeId is null or st.storeId = :storeId)
          and (:status is null or sh.shipmentStatus = :status)
          and (:fromDt is null or so.orderDatetime >= :fromDt)
          and (:toDt is null or so.orderDatetime <= :toDt)
        group by so.storeOrderId, so.orderNo, st.storeName, sh.shipmentStatus, so.orderDatetime
        order by so.orderDatetime desc
        """,
            countQuery = """
        select count(distinct so.storeOrderId)
        from StoreOrder so
            join so.store st
            left join Shipment sh on sh.storeOrder.storeOrderId = so.storeOrderId
            left join StoreOrderDetail sod on sod.storeOrder.storeOrderId = so.storeOrderId
        where (:orderNo is null or so.orderNo like concat('%', :orderNo, '%'))
          and (:storeId is null or st.storeId = :storeId)
          and (:status is null or sh.shipmentStatus = :status)
          and (:fromDt is null or so.orderDatetime >= :fromDt)
          and (:toDt is null or so.orderDatetime <= :toDt)
        """
    )
    Page<ShipmentResponseDto> findPage(
            @Param("orderNo") String orderNo,
            @Param("storeId") Long storeId,
            @Param("status") ShipmentStatus status,
            @Param("fromDt") LocalDateTime from,
            @Param("toDt") LocalDateTime to,
            Pageable pageable
    );
}
