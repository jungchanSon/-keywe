package com.kiosk.server.banking.adaptor.inbound.kafka;

import com.kiosk.server.banking.adaptor.inbound.kafka.mapper.OrderCreatedEventMapper;
import com.kiosk.server.banking.application.port.inbound.DepositCommand;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import com.kiosk.server.util.KafkaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventConsumer {

    private final DepositCommand depositCommand;
    private final OrderCreatedEventMapper orderCreatedEventMapper;

    public void consumeOrderCreatedEvent(String message) {
        OrderEvent.OrderCreatedEvent orderCreatedEvent = KafkaEventMapper.toObject(message, OrderEvent.OrderCreatedEvent.class);

        log.info("[뱅킹] 정산 시작: shopId={}, amount={}", orderCreatedEvent.shopId(), orderCreatedEvent.totalPrice());
        depositCommand.deposit(orderCreatedEventMapper.toVO(orderCreatedEvent));
        log.info("[뱅킹] 정산 완료: shopId={}, amount={}", orderCreatedEvent.shopId(), orderCreatedEvent.totalPrice());
    }

}
