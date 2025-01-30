package com.kiosk.server.store.service;

import org.springframework.web.multipart.MultipartFile;

public interface CreateCategoryService {

    int doService(String categoryName, MultipartFile file);
}
