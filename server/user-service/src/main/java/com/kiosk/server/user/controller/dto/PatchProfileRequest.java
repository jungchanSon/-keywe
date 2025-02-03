package com.kiosk.server.user.controller.dto;

public record PatchProfileRequest(String name, String phone, String password) {
}
