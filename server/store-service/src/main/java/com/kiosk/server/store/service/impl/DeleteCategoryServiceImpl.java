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
            throw new EntityNotFoundException("삭제하려는 카테고리를 찾을 수 없습니다. 카테고리 정보를 확인해 주세요.");
        }

        if (userId != category.getUserId()) {
            throw new UnauthorizedException("삭제 권한이 없습니다. 요청이 유효한지 다시 확인해 주세요.");
        }

        categoryRepository.deleteCategory(categoryId);
    }
}
