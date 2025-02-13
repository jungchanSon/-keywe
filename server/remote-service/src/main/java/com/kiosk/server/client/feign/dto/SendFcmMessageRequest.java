package com.kiosk.server.client.feign.dto;

public record SendFcmMessageRequest(String targetType, Long targetId, NotificationMessage message) {
}
