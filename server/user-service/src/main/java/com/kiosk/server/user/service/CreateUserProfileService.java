package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.CreateProfileResponse;

public interface CreateUserProfileService {

    CreateProfileResponse doService(long userId, String profileName, String profileRole, String phoneNumber, String profilePass);
}
