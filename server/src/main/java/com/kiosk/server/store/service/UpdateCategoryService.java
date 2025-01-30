package com.kiosk.server.store.service;

import org.springframework.web.multipart.MultipartFile;

public interface UpdateCategoryService {

    void doService(String categoryName, MultipartFile image, int categoryId);
}
