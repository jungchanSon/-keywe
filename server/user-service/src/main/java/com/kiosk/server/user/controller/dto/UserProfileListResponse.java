package com.kiosk.server.user.controller.dto;

import com.kiosk.server.user.domain.ProfileRole;

public record UserProfileListResponse(String id, String name, ProfileRole role, String image) {
}
