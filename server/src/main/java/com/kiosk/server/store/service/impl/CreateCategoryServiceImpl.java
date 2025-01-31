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
    public long doService(long userId, String categoryName) {

        StoreMenuCategory category = StoreMenuCategory.create(userId, categoryName);

        categoryRepository.insertCategory(category);

        return category.getCategoryId();
    }
}
