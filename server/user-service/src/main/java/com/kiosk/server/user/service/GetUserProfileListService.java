package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.UserProfileListResponse;

import java.util.List;

public interface GetUserProfileListService {

    List<UserProfileListResponse> doService(long userId);
}
