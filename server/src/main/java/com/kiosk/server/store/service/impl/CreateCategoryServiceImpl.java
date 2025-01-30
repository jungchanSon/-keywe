package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.Images;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.CreateCategoryService;
import com.kiosk.server.store.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CreateCategoryServiceImpl implements CreateCategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageUploadService imageUploadService;

    @Override
    public int doService(String categoryName, MultipartFile file) {

        // 이미 존재하는 카테고리인지 확인
        if (categoryRepository.findCategoryIdByName(categoryName) != null) {
            throw new BadRequestException("Category already exists");
        }

        Images image = null;
        // 이미지 파일 있는경우 업로드 처리
        if (file != null && !file.isEmpty()) {
            image = imageUploadService.doService(file);
        }

        // 이미지 없으면 기본값(0) 설정
        int imgId = (image == null ? 0 : image.getImageId());

        StoreMenuCategory category = StoreMenuCategory.create(categoryName, imgId);

        categoryRepository.insertCategory(category);

        return category.getCategoryId();
    }
}
