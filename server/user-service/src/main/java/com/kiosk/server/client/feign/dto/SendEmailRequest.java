package com.kiosk.server.client.feign.dto;

public record SendEmailRequest(String to, String subject, String content) {
}
