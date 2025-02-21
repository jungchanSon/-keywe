package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.banking.domain.Withdraw;
import com.kiosk.server.infra.rdb.entity.WithdrawEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawRepository extends JpaRepository<WithdrawEventEntity, Long> {
    Optional<WithdrawEventEntity> findByUserId(Long userId);

    List<WithdrawEventEntity> findDepositEventEntitiesByUserIdAndWithdrawEventIdGreaterThanOrderByCreatedAt (Long userId, Long snapShot);
}
