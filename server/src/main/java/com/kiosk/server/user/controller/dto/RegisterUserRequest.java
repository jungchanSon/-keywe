package com.kiosk.server.user.controller.dto;

public record RegisterUserRequest(String role, String email, String password) {
}
