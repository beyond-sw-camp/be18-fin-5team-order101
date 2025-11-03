package com.synerge.order101.inbound.model.repository;

import com.synerge.order101.inbound.model.entity.InBoundDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundRepository extends JpaRepository<InBoundDetail,Long> {

}
