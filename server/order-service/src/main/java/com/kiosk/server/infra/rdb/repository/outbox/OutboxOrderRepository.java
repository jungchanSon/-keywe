package com.kiosk.server.infra.rdb.repository.outbox;

import com.kiosk.server.infra.rdb.entity.outbox.OutBoxStatus;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxOrderRepository extends JpaRepository<OutboxOrderEntity, Long> {
    List<OutboxOrderEntity> findAllByOutBoxStatus(OutBoxStatus outboxStatus);
}
