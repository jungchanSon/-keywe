package com.kiosk.server.infra.rdb.repository;

import com.kiosk.server.infra.rdb.entity.OutBoxStatus;
import com.kiosk.server.infra.rdb.entity.ReceiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiveRepository extends JpaRepository<ReceiveEntity, Long> {

    List<ReceiveEntity> findAllByOutboxStatus(OutBoxStatus outboxStatus);
}
