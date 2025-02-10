package com.kiosk.server.store.domain;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MenuRepository {

    // 카테고리 명으로 카테고리 id 조회
    Long findCategoryIdByName(@Param("userId") long userId, @Param("categoryName") String categoryName);

    // 메뉴 등록
    void insert(StoreMenu menu);

    // 옵션 저장
    void insertOptions(List<StoreMenuOption> optionParams);

    // 메뉴 수정
    void update(Map<String, Object> updateParams);

    // 옵션 수정
    void updateOption(Map<String, Object> updateOption);

    // 메뉴 단건 상세조회
    StoreMenu findDetailById(@Param("userId")long userId, @Param("menuId") long menuId);

    // 특정 메뉴 옵션 리스트 조회
    List<StoreMenuOption> findDetailOptionsById(long menuId);

    StoreMenuOption findOptionById(@Param("menuId")long menuId, @Param("optionId") long optionId);

    // 모든 메뉴 조회
    List<StoreMenu> findList(long userId);

    // 특정 카테고리 메뉴 조회
    List<StoreMenu> findByCategory(@Param("userId")long userId, @Param("categoryId")long categoryId);

    // 메뉴 삭제
    void deleteById(long menuId);

    // 개별 옵션 삭제
    void deleteOptionById(long optionId);

    // 옵션 그룹 삭제
    void deleteOptionGroupById(long optionGroupId);

    boolean existsOptionById(long optionId);

    boolean existsOptionGroupId(long menuId, long optionGroupId);

}
