package com.kiosk.server.user.controller.dto;

public record CreateProfileRequest(String type, String name, String phone, String password) {
}
