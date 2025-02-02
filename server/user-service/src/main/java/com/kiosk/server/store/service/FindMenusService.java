package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.MenuDetailResponse;

import java.util.List;

public interface FindMenusService {

    List<MenuDetailResponse> doService(long userId, Long categoryId);
}
