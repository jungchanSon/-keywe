package com.kiosk.server.infra.kafka.producer;

import com.kiosk.server.infra.kafka.event.OrderCompensatingEvent;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import com.kiosk.server.infra.rdb.entity.outbox.OutBoxStatus;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderEntity;
import com.kiosk.server.order.adaptor.outbound.OutboxCommandAdaptor;
import com.kiosk.server.order.application.port.outbound.OutboxCommandPort;
import com.kiosk.server.order.application.port.outbound.OutboxQueryPort;
import com.kiosk.server.order.domain.OutboxOrder;
import com.kiosk.server.order.domain.mapper.OutboxOrderMapper;
import com.kiosk.server.util.KafkaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private static final String CREATE_ORDER_TOPIC = "create-order";
    private static final String COMPENSATING_TOPIC = "order-rollback-user-withdraw";

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final OutboxOrderMapper outboxOrderMapper;
    private final OutboxCommandAdaptor outboxCommandPort;
    private final OutboxQueryPort outboxQueryPort;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void outboxOrderCreated(OrderEvent.OrderCreatedEvent orderCreatedEvent) {

        OutboxOrderEntity outboxOrderEntity = outboxOrderMapper.toOutboxOrderEntity(orderCreatedEvent, OutBoxStatus.PENDING);
        outboxCommandPort.saveOutboxOrder(outboxOrderEntity);

        List<OrderEvent.Menu> menuList = orderCreatedEvent.menuList();

        menuList.forEach(
                menu -> {
                    outboxCommandPort.saveOutboxOrderMenu(outboxOrderMapper.toOutboxOrderMenuEntity(menu));
                    menu.optionList().forEach(
                            option -> {
                                outboxCommandPort.saveOutboxOrderMenuOption(outboxOrderMapper.toOutboxOrderMenuOptionEntity(option));
                            }
                    );
                }
        );
    }

    @Transactional(propagation = REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendUserCreatedEvent(OrderEvent.OrderCreatedEvent orderCreatedEvent) {
        kafkaTemplate.send(CREATE_ORDER_TOPIC, KafkaEventMapper.toEvent(orderCreatedEvent))
                .whenComplete((result, exception) -> {
                    if(exception == null) {
                        OutboxOrder order = outboxQueryPort.findById(orderCreatedEvent.orderId());
                        order.setOutBoxStatus(OutBoxStatus.COMPLETED);
                        outboxCommandPort.saveOutboxOrder(outboxOrderMapper.toEntityFromDomain(order));
                        log.info("[주문] 주문 이벤트 발행 성공 orderId={}", order.getOrderId());
                    } else {

                        log.info("[주문] 주문 이벤트 발행 실패 orderId={}", orderCreatedEvent.orderId());
                    }
                });
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void sendCompensatingEvent(OrderCompensatingEvent.CompensatingEvent event) {

        log.info("주문 실패 보상 트랜잭션 이벤트 발행 userId={}, amount={}", event.userId(), event.amount());
        kafkaTemplate.send(COMPENSATING_TOPIC, KafkaEventMapper.toEvent(event));
    }
}
