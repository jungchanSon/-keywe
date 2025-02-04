package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.MenuOptionRequest;

public interface DeleteOptionService {

    void doService(long userId, long menuId, long optionId, MenuOptionRequest request);
}
