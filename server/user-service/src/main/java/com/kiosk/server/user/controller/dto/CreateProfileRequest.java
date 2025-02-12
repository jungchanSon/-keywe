package com.kiosk.server.user.controller.dto;

public record CreateProfileRequest(String role, String name, String phone, String password) {
}
