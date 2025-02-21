package com.kiosk.server.user.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SmsRequest (@NotBlank(message = "전화번호는 필수입니다.") String phone) {
}
