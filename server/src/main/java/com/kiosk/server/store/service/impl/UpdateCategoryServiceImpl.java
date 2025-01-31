package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.UpdateCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateCategoryServiceImpl implements UpdateCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void doService(String categoryName, int categoryId) {

        // 기존 카테고리 조회
        StoreMenuCategory category = fetchCategory(categoryId);
        String updatedName = (categoryName != null) ? categoryName : category.getCategoryName();

        // 업데이트 할 데이터 매핑
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("categoryId", categoryId);
        updateParams.put("categoryName", updatedName);

        categoryRepository.updateCategory(updateParams);
    }

    private StoreMenuCategory fetchCategory(int categoryId) {

        StoreMenuCategory category = categoryRepository.findCategoryById(categoryId);
        if (category == null) {
            throw new BadRequestException("Category not found");
        }
        return category;
    }

}
