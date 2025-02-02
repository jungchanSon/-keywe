package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.UpdateMenuRequest;
import org.springframework.web.multipart.MultipartFile;


public interface UpdateMenuService {

    void doService(long userId, long menuId, UpdateMenuRequest updateMenu, MultipartFile image);
}
