package com.kiosk.server.client.feign.dto;

public record SendFcmMessageResponse(int successCount, int failureCount) {
}
