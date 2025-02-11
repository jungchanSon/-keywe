package com.kiosk.server.user.service;

import com.kiosk.server.user.controller.dto.ChildProfileResponse;

import java.util.List;

public interface FindChildProfileListService {

    List<ChildProfileResponse> doService(long userId);
}
