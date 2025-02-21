package com.kiosk.server.infra.kafka.event;

public class MemberEvent {

    public record CreatedEvent(
            Long memberId,
            String email
    ) { }
}
