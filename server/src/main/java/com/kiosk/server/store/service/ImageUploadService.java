package com.kiosk.server.store.service;

import com.kiosk.server.store.domain.Images;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {

    Images doService(MultipartFile file);
}
