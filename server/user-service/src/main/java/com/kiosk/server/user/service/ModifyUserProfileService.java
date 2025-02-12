package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.PatchProfileResponse;

public interface ModifyUserProfileService {

    PatchProfileResponse doService (long userId, long profileId, String profileName, String phoneNumber, String profilePass);
}
