package com.kiosk.server.banking.adaptor.inbound.kafka;

import com.kiosk.server.banking.adaptor.inbound.kafka.mapper.MemberEventMapper;
import com.kiosk.server.banking.application.port.inbound.AccountCommand;
import com.kiosk.server.infra.kafka.event.MemberEvent;
import com.kiosk.server.util.KafkaEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberCreatedEventConsumer {

    private final AccountCommand accountCommand;

    private final MemberEventMapper memberEventMapper;

    @KafkaListener(topics = "create-member", groupId = "banking-service")
    public void consumeMemberCreatedEvent(String message) {
        MemberEvent.CreatedEvent createdMemberEvent = KafkaEventMapper.toObject(message, MemberEvent.CreatedEvent.class);
        log.info("[회원가입 이벤트] 잔고 초기화 시작 : userId={} , email={}",
                createdMemberEvent.memberId(),
                createdMemberEvent.email()
        );

        long accountId = accountCommand.create(memberEventMapper.toAccountVO(createdMemberEvent));

        log.info("[잔고 설정] userId={}, accountId={}", createdMemberEvent.memberId(), accountId);
    }
}
