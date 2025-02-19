package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    AccountEntity findByMemberId(Long memberId);
    List<AccountEntity> getAccountEntitiesByMemberId(Long memberId);
}
