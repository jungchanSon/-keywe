package com.kiosk.server.kafka.producer;

import com.kiosk.server.kafka.event.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {

    private static final String TOPIC = "create-member";
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendUserCreatedEvent(UserEvent.UserCreatedEvent userEvent) {

        log.info("회원가입 성공 이벤트 발행: userId:{}", userEvent.memberId());
        kafkaTemplate.send(TOPIC, userEvent.toString());
    }
}
