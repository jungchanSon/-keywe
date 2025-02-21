package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.OrderMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenuEntity, Long> {
}
