package com.kiosk.server.websocket.message.response;

import com.kiosk.server.websocket.exception.RemoteOrderError;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Builder
public record RemoteOrderResponse(
    RemoteOrderResponseType type,
    Map<String, Object> data,
    String timestamp
) {

    public RemoteOrderResponse {
        if (timestamp == null) {
            timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        }
    }

    public static RemoteOrderResponse success(RemoteOrderResponseType type) {
        return success(type, Map.of());
    }

    public static RemoteOrderResponse success(RemoteOrderResponseType type, Map<String, Object> data) {
        return RemoteOrderResponse.builder()
            .type(type)
            .data(data)
            .build();
    }

    public static RemoteOrderResponse error(RemoteOrderError error) {
        return RemoteOrderResponse.builder()
            .type(RemoteOrderResponseType.ERROR)
            .data(error.toMap())
            .build();
    }
}
