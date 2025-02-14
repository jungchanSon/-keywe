package com.kiosk.server.user.controller.dto;

import com.kiosk.server.user.domain.UserRole;

public record LoginResponse(String accessToken, UserRole role) {
}
