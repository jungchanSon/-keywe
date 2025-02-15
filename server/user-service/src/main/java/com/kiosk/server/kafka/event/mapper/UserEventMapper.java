package com.kiosk.server.kafka.event.mapper;

import com.kiosk.server.kafka.event.UserEvent;
import com.kiosk.server.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserEventMapper {

    public UserEvent.UserCreatedEvent toUserCreatedEvent(User user) {
        return UserEvent.UserCreatedEvent.builder()
                .email(user.getEmail())
                .memberId(user.getUserId())
                .build();
    }
}
