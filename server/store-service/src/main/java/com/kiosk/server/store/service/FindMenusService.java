package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.MenuResponse;

import java.util.List;

public interface FindMenusService {

    List<MenuResponse> doService(long userId, Long categoryId);
}
