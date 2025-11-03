package com.synerge.order101.outbound.model.repository;

import com.synerge.order101.outbound.model.entity.OutBound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutBoundRepository extends JpaRepository<OutBound,Long> {
}
