package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.MenuDetailResponse;

public interface FindMenuDetailService {

    MenuDetailResponse doService(long userId, long menuId);
}
