package com.kiosk.server.banking.adaptor.inbound.kafka;

import com.kiosk.server.banking.application.port.inbound.AccountCommand;
import com.kiosk.server.banking.application.port.inbound.DepositCommand;
import com.kiosk.server.banking.application.service.banking.DepositWriter;
import com.kiosk.server.banking.application.service.banking.vo.DepositVO;
import com.kiosk.server.infra.kafka.event.OrderCompensatingEvent;
import com.kiosk.server.util.KafkaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCompensatedEventConsumer {

    private static final String COMPENSATING_TOPIC = "order-rollback-user-withdraw";
    private final DepositCommand depositCommand;

    @KafkaListener(topics = COMPENSATING_TOPIC, groupId = "banking-service")
    public void consumeOrderCompensatedEvent(String message) {
        OrderCompensatingEvent.CompensatingEvent event = KafkaEventMapper.toObject(message, OrderCompensatingEvent.CompensatingEvent.class);

        log.info("[뱅킹] 보상 트랜젝션 시작: userId={}, amount={}", event.userId(), event.amount());
        depositCommand.deposit(
                DepositVO.Save.builder()
                        .userId(event.userId())
                        .amount(event.amount())
                        .build()
        );
        log.info("[뱅킹] 보상 트랜잭션 완료: userId={}, amount={}", event.userId(), event.amount());
    }

}
