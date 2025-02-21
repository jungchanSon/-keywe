package com.kiosk.server.store.service.impl;

import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.CreateCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCategoryServiceImpl implements CreateCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public long doService(long userId, String categoryName) {
        log.info("CreateCategoryService: userId={}, categoryName={}", userId, categoryName);

        StoreMenuCategory category = StoreMenuCategory.create(userId, categoryName);

        categoryRepository.insertCategory(category);
        log.info("카테고리 생성 완료 - userId={}, categoryId={}, categoryName={}",
                userId, category.getCategoryId(), categoryName);

        return category.getCategoryId();
    }
}
