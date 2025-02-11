package com.kiosk.server.store.domain;

import com.kiosk.server.store.controller.dto.FindAllCategoriesResponse;

import java.util.List;
import java.util.Map;

public interface CategoryRepository {

    // 주어진 카테고리 ID로 카테고리 조회
    StoreMenuCategory findCategoryById(long categoryId);

    // 카테고리 등록
    void insertCategory(StoreMenuCategory category);

    // 카테고리 모두 조회
    List<FindAllCategoriesResponse> findAllCategoriesByUserId(long userId);

    // 카테고리 수정
    void updateCategory(Map<String, Object> updateParams);

    // 카테고리 삭제
    void deleteCategory(long categoryId);

    boolean existsById(long categoryId);
}
