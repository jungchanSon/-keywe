package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.CreateCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryServiceImpl implements CreateCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public int doService(String categoryName) {

        // 이미 존재하는 카테고리인지 확인
        if (categoryRepository.findCategoryIdByName(categoryName) != null) {
            throw new BadRequestException("Category already exists");
        }

        StoreMenuCategory category = StoreMenuCategory.create(categoryName);

        categoryRepository.insertCategory(category);

        return category.getCategoryId();
    }
}
