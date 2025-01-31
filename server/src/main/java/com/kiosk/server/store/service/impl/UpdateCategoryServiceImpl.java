package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.UpdateCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UpdateCategoryServiceImpl implements UpdateCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void doService(long userId, long categoryId, String newCategoryName) {
        // 기존 카테고리 조회
        validateCategoryId(categoryId);

        if (!StringUtils.hasLength(newCategoryName)) {
            throw new BadRequestException("Category name is empty");
        }

        // 업데이트 할 데이터 매핑
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("categoryId", categoryId);
        updateParams.put("categoryName", newCategoryName);

        categoryRepository.updateCategory(updateParams);
    }

    private void validateCategoryId(long categoryId) {
        StoreMenuCategory category = categoryRepository.findCategoryById(categoryId);
        if (category == null) {
            throw new EntityNotFoundException("Category not found");
        }
    }
}
