package com.kiosk.server.dto;

import java.util.HashMap;
import java.util.Map;

public record NotificationMessage(
    String title,
    String body,
    String channelId,
    Map<String, String> data,
    MessagePriority priority,
    Integer timeToLive
) {
    public enum MessagePriority {
        HIGH, NORMAL
    }

    public NotificationMessage {
        if (data == null) {
            data = new HashMap<>();
        }
    }
}
