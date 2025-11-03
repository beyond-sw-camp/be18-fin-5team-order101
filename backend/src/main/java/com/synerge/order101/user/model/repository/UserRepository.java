package com.synerge.order101.user.model.repository;

import com.synerge.order101.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
