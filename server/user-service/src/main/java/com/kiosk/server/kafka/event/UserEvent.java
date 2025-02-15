package com.kiosk.server.kafka.event;

import lombok.Builder;

public class UserEvent {

    @Builder
    public record UserCreatedEvent(
            Long memberId,
            String email
    ) { }
}
