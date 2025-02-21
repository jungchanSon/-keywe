package com.kiosk.server.store.domain;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MenuRepository {

    // 카테고리 명으로 카테고리 id 조회
    Long findCategoryIdByName(@Param("userId") long userId, @Param("categoryName") String categoryName);

    // 메뉴 등록
    void insert(StoreMenu menu);

    // 메뉴 수정
    void update(Map<String, Object> updateParams);

    // 메뉴 단건 상세조회
    StoreMenu findById(@Param("userId") long userId, @Param("menuId") long menuId);

    // 모든 메뉴 조회
    List<StoreMenu> findAll(long userId);

    // 특정 카테고리 메뉴 조회
    List<StoreMenu> findByCategory(@Param("userId") long userId, @Param("categoryId") long categoryId);

    // 메뉴 삭제
    void deleteById(long menuId);

}
