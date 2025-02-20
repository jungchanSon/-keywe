package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.UserProfileResponse;

public interface FindUserProfileService {

    UserProfileResponse doService(Long profileId);
}
