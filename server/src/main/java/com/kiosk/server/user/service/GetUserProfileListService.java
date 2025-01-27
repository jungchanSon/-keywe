package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.UserProfileResponse;

import java.util.List;

public interface GetUserProfileListService {

    List<UserProfileResponse> doService(long userId);
}
