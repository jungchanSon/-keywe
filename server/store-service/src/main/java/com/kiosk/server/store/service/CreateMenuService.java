package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.CreateMenuRequest;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CreateMenuService {

    CreateMenuResponse doService(long userId, CreateMenuRequest request, MultipartFile image);
}
