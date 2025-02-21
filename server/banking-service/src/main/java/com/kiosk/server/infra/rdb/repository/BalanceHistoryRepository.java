package com.kiosk.server.infra.rdb.repository;


import com.kiosk.server.infra.rdb.entity.BalanceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceHistoryRepository extends JpaRepository<BalanceHistoryEntity, Long> {
}
