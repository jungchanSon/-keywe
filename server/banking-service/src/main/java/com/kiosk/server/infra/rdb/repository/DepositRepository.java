package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.DepositEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepositRepository extends JpaRepository<DepositEventEntity, Long> {
    Optional<DepositEventEntity> getByUserId(Long userId);

    List<DepositEventEntity> findDepositEventEntitiesByUserIdAndDepositEventIdGreaterThanOrderByLocalDateTimeAsc(Long userId, Long snapShot);
}
