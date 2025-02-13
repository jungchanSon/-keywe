package com.kiosk.server.websocket.message;

import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Builder
public record RemoteOrderResponseMessage(
    RemoteOrderResponseMessageType type,
    Map<String, Object> data,
    String timestamp
) {

    public RemoteOrderResponseMessage {
        if (timestamp == null) {
            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        }
    }

    public static RemoteOrderResponseMessage success(RemoteOrderResponseMessageType type) {
        return success(type, Map.of());
    }

    public static RemoteOrderResponseMessage success(RemoteOrderResponseMessageType type, Map<String, Object> data) {
        return RemoteOrderResponseMessage.builder()
            .type(type)
            .data(data)
            .build();
    }

    public static RemoteOrderResponseMessage error(RemoteServiceError error) {
        return RemoteOrderResponseMessage.builder()
            .type(RemoteOrderResponseMessageType.ERROR)
            .data(error.toMap())
            .build();
    }
}
