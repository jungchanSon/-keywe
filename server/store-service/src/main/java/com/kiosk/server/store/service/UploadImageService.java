package com.kiosk.server.store.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {

    void doService(long userId, long menuId, MultipartFile file);
}
