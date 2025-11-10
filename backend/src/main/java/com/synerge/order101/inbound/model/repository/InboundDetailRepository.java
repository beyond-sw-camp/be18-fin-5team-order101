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
}
