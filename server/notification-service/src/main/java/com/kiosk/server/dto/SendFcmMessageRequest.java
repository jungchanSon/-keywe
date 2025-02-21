package com.kiosk.server.dto;

import java.util.List;

public record SendFcmMessageRequest(TargetType targetType, List<Long> targetIds, NotificationMessage message) {
}
