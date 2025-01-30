package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.service.DeleteCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryServiceImpl implements DeleteCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void doService(String role, int categoryId) {

        // 현재는 단순한 역할 확인 로직이지만,
        // 추후 '사장님(CEO)' 로그인 기능이 추가되면
        // 토큰에서 역할(UserRole.Role)을 추출하여 검증하는 방식으로 개선 예정.
        if (!"CEO".equals(role)) {
            throw new UnauthorizedException("Not allowed to do delete category");
        }

        if (categoryRepository.findCategoryById(categoryId) == null) {
            throw new EntityNotFoundException("Category not found");
        }

        categoryRepository.deleteCategory(categoryId);

    }
}