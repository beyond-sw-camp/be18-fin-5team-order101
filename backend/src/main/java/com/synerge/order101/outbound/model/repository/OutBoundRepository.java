package com.synerge.order101.outbound.model.repository;

import com.synerge.order101.outbound.model.entity.OutBound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutBoundRepository extends JpaRepository<OutBound,Long> {
}
