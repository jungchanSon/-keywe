package com.kiosk.infra.rdb.repository;

import com.kiosk.infra.rdb.entity.OrderMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenuEntity, Long> {
}
