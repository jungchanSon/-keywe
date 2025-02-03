package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.UserProfileDetailResponse;

public interface ProfileDetailService {

    UserProfileDetailResponse doService(long userId, long profileId);
}
