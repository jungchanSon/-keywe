package com.kiosk.server.client.feign.dto;

import java.util.List;

public record SendFcmMessageRequest(String targetType, List<String> targetIds, NotificationMessage message) {
}
