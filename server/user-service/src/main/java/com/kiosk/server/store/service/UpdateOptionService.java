package com.kiosk.server.store.service;


import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.MenuOptionRequest;

public interface UpdateOptionService {

    CreateMenuResponse doService(long userId, long menuId, long optionId, MenuOptionRequest request);
}
