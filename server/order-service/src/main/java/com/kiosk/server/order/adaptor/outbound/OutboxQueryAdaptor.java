package com.kiosk.server.order.adaptor.outbound;

import com.kiosk.server.infra.rdb.entity.outbox.OutBoxStatus;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuOptionEntity;
import com.kiosk.server.infra.rdb.repository.outbox.OutboxOrderMenuOptionRepository;
import com.kiosk.server.infra.rdb.repository.outbox.OutboxOrderMenuRepository;
import com.kiosk.server.infra.rdb.repository.outbox.OutboxOrderRepository;
import com.kiosk.server.order.application.port.outbound.OutboxQueryPort;
import com.kiosk.server.order.domain.OutboxMenu;
import com.kiosk.server.order.domain.OutboxOption;
import com.kiosk.server.order.domain.OutboxOrder;
import com.kiosk.server.order.domain.mapper.OutboxOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OutboxQueryAdaptor implements OutboxQueryPort {

    private final OutboxOrderRepository outboxOrderRepository;
    private final OutboxOrderMenuRepository outboxOrderMenuRepository;
    private final OutboxOrderMenuOptionRepository outboxOrderMenuOptionRepository;
    private final OutboxOrderMapper outboxOrderMapper;

    @Override
    public OutboxOrder findById(Long id) {
        return outboxOrderMapper.toOutboxOrderDomain(outboxOrderRepository.findById(id).orElseThrow());
    }

    @Override
    public List<OutboxOrder> searchOutboxOrderPending() {
        return outboxOrderRepository.findAllByOutBoxStatus(OutBoxStatus.PENDING)
                .stream()
                .map(outboxOrderMapper::toOutboxOrderDomain)
                .toList();
    }

    @Override
    public List<OutboxMenu> searchOutboxMenuByOrderId(Long orderId) {
        return outboxOrderMenuRepository.findAllByOrderId(orderId)
                .stream()
                .map(outboxOrderMapper::toOutboxMenuDomain)
                .toList();
    }

    @Override
    public List<OutboxOption> searchOutboxOptionByOrderId(Long orderId) {
        return outboxOrderMenuOptionRepository.findAllByOrderId(orderId)
                .stream()
                .map(outboxOrderMapper::toOutboxOptionDomain)
                .toList();
    }
}
