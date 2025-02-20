package com.kiosk.server.order.application.port.outbound;

import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderEntity;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuEntity;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuOptionEntity;

public interface OutboxCommandPort {

    void saveOutboxOrder(OutboxOrderEntity outboxOrderEntity);
    void saveOutboxOrderMenu(OutboxOrderMenuEntity outboxOrderMenuEntity);
    void saveOutboxOrderMenuOption(OutboxOrderMenuOptionEntity outboxOrderMenuOptionEntity);
}
