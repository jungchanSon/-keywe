package com.kiosk.server.dto;

public record SendEmailRequest(String to, String subject, String content) {
}
