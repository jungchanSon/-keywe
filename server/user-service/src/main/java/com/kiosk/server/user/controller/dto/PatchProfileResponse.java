package com.kiosk.server.user.controller.dto;

import com.kiosk.server.user.domain.ProfileRole;

public record PatchProfileResponse(ProfileRole role, String name, String phone, String password) {
}
