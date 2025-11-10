package com.synerge.order101.inbound.model.repository;

import com.synerge.order101.inbound.model.entity.Inbound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InboundRepository extends JpaRepository<Inbound,Long> {

    @Query("""
        select i as inbound,
               (select count(distinct d.product.productId)
                from InboundDetail d
                where d.inbound = i) as itemCount,
               (select coalesce(sum(d.receivedQty), 0)
                from InboundDetail d
                where d.inbound = i) as totalQty
        from Inbound i
        join i.supplier s
        order by i.inboundDatetime desc, i.inboundId desc
        """
    )
    Page<Object[]> findInboundWithCounts(Pageable pageable);
}
