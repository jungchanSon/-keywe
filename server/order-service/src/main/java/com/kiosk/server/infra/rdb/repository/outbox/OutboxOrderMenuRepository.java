package com.kiosk.server.infra.rdb.repository.outbox;

import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxOrderMenuRepository extends JpaRepository<OutboxOrderMenuEntity, Long> {
    List<OutboxOrderMenuEntity> findAllByOrderId(long orderId);
}
