package com.synerge.order101.inbound.model.repository;

import com.synerge.order101.inbound.model.entity.InBound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboundDetailRepository extends JpaRepository<InBound,Long> {

}
