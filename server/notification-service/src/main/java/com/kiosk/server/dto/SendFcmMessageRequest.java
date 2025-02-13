package com.kiosk.server.dto;

public record SendFcmMessageRequest(TargetType targetType, Long targetId, NotificationMessage message) {
}
