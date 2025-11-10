package com.synerge.order101.outbound.model.repository;

import com.synerge.order101.outbound.model.entity.OutboundDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboundDetailRepository extends JpaRepository<OutboundDetail, Long> {
    @Query("""
        select d
        from OutboundDetail d
        join fetch d.product p
        join fetch d.outbound o
        where o.outboundId = :outboundId
        order by d.outboundDetailId asc
    """)
    List<OutboundDetail> findByOutbound(@Param("outboundId") Long outboundId);

}
