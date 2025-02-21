package com.kiosk.server.infra.kafka.event;

public class OrderCompensatingEvent {

    public record CompensatingEvent(
            Long userId,
            Long amount
    ) { }

}
