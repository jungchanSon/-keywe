package com.kiosk.infra.rdb.repository;

import com.kiosk.infra.rdb.entity.OrderMenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuOptionRepository extends JpaRepository<OrderMenuOptionEntity, Long> {
}
