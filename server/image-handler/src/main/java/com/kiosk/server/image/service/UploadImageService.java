package com.kiosk.server.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {

    void doService(long userId, long targetId, MultipartFile file, String type);
}
