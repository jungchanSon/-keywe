package com.kiosk.server.infra.rdb.repository.outbox;

import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuOptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxOrderMenuOptionRepository extends JpaRepository<OutboxOrderMenuOptionEntity, Long> {
    List<OutboxOrderMenuOptionEntity> findAllByOrderId(long orderId);
}
