package com.kiosk.server.user.service;

import com.kiosk.server.user.domain.UserProfile;

public interface ModifyUserProfileService {

    UserProfile doService (long userId, long profileId, String profileName, String phoneNumber, String profilePass);
}
