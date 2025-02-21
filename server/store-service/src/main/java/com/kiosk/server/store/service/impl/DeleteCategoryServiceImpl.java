package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.StoreMenuCategory;
import com.kiosk.server.store.service.DeleteCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteCategoryServiceImpl implements DeleteCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void doService(long userId, long categoryId) {
        log.info("DeleteCategoryService: userId={}, categoryId={}", userId, categoryId);

        StoreMenuCategory category = categoryRepository.findCategoryById(categoryId);
        if (category == null) {
            log.warn("삭제할 카테고리 없음 - userId={}, categoryId={}", userId, categoryId);
            throw new EntityNotFoundException("삭제하려는 카테고리를 찾을 수 없습니다. 카테고리 정보를 확인해 주세요.");
        }

        if (userId != category.getUserId()) {
            log.warn("카테고리 삭제 권한 없음 - userId={}, categoryId={}", userId, categoryId);
            throw new UnauthorizedException("삭제 권한이 없습니다. 요청이 유효한지 다시 확인해 주세요.");
        }

        categoryRepository.deleteCategory(categoryId);
        log.info("카테고리 삭제 완료 - userId={}, categoryId={}", userId, categoryId);
    }
}
