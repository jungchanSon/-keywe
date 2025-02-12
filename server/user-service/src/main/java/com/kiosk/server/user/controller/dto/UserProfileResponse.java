package com.kiosk.server.user.controller.dto;

import com.kiosk.server.user.domain.ProfileRole;

public record UserProfileResponse(String id, String name, ProfileRole role) {
}
