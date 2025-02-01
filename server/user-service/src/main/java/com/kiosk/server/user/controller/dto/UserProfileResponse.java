package com.kiosk.server.user.controller.dto;

import com.kiosk.server.user.domain.ProfileRole;

public record UserProfileResponse(long id, String name, String phone, ProfileRole type) {
}
