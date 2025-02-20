package com.kiosk.server.order.adaptor.outbound;

import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderEntity;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuEntity;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuOptionEntity;
import com.kiosk.server.infra.rdb.repository.outbox.OutboxOrderMenuOptionRepository;
import com.kiosk.server.infra.rdb.repository.outbox.OutboxOrderMenuRepository;
import com.kiosk.server.infra.rdb.repository.outbox.OutboxOrderRepository;
import com.kiosk.server.order.application.port.outbound.OutboxCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class OutboxCommandAdaptor implements OutboxCommandPort {

    private final OutboxOrderRepository outboxOrderRepository;
    private final OutboxOrderMenuRepository outboxOrderMenuRepository;
    private final OutboxOrderMenuOptionRepository outboxOrderMenuOptionRepository;

    @Override
    @Transactional
    public void saveOutboxOrder(OutboxOrderEntity outboxOrderEntity) {
        outboxOrderRepository.saveAndFlush(outboxOrderEntity);
    }

    @Override
    @Transactional
    public void saveOutboxOrderMenu(OutboxOrderMenuEntity outboxOrderMenuEntity) {
        outboxOrderMenuRepository.save(outboxOrderMenuEntity);
    }

    @Override
    @Transactional
    public void saveOutboxOrderMenuOption(OutboxOrderMenuOptionEntity outboxOrderMenuOptionEntity) {
        outboxOrderMenuOptionRepository.save(outboxOrderMenuOptionEntity);
    }
}
