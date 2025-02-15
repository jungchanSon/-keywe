package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.OrderMenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuOptionRepository extends JpaRepository<OrderMenuOptionEntity, Long> {
}
