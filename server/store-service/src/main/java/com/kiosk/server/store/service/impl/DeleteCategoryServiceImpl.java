package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.DeleteCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryServiceImpl implements DeleteCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void doService(long userId, long categoryId) {

        StoreMenuCategory category = categoryRepository.findCategoryById(categoryId);
        if (category == null) {
            throw new EntityNotFoundException("Invalid category id");
        }

        if (userId != category.getUserId()) {
            throw new UnauthorizedException("You do not have permission to delete this category");
        }

        categoryRepository.deleteCategory(categoryId);
    }
}
