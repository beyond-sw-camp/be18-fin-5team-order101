package com.synerge.order101.inbound.model.repository;

import com.synerge.order101.inbound.model.entity.InboundDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InboundDetailRepository extends JpaRepository<InboundDetail ,Long> {

    @Query("""
        select d
        from InboundDetail d
        join fetch d.product p
        join fetch d.inbound i
        where i.inboundId = :inboundId
        order by d.inboundDetailId asc
    """)
    List<InboundDetail> findByInbound(@Param("inboundId") Long inboundId);

    @Query(value = """
      (
        SELECT 
          i.inbound_no      AS movement_no,
          '입고'            AS type,
          d.received_qty    AS qty,
          i.inbound_datetime AS occurred_at
        FROM inbound_detail d
        JOIN inbound i ON i.inbound_id = d.inbound_id
        WHERE d.product_id = :pid
      )
      UNION ALL
      (
        SELECT 
          o.outbound_no     AS movement_no,
          '출고'            AS type,
          d.outbound_qty    AS qty,
          o.outbound_datetime AS occurred_at
        FROM outbound_detail d
        JOIN outbound o ON o.outbound_id = d.outbound_id
        WHERE d.product_id = :pid
      )
      ORDER BY occurred_at DESC
      LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Object[]> findMovements(@Param("pid") Long productId,
                                 @Param("limit") int limit,
                                 @Param("offset") int offset);

    @Query(value = """
        SELECT (SELECT COUNT(*) FROM inbound_detail d
                JOIN inbound i ON i.inbound_id = d.inbound_id
                WHERE d.product_id = :pid)
                +
            (SELECT COUNT(*) FROM outbound_detail d
                JOIN outbound o ON o.outbound_id = d.outbound_id
                WHERE d.product_id = :pid)
""", nativeQuery = true)
    long countMovements(@Param("pid") Long productId);
}
