package com.kiosk.server.user.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record SmsVerifyRequest(@NotBlank(message = "전화번호는 필수입니다.") String phone,
                               @NotBlank(message = "인증번호를 입력해주세요.") String verificationCode) {
}
