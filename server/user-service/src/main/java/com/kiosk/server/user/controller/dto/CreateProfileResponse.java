package com.kiosk.server.user.controller.dto;

import com.kiosk.server.user.domain.ProfileRole;

public record CreateProfileResponse(String id, ProfileRole role, String createAt) {
}
