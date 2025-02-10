package com.kiosk.server.store.domain;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MenuOptionRepository {

    // 옵션 저장
    void insert(List<StoreMenuOption> optionParams);

    // 옵션 수정
    void update(Map<String, Object> updateOption);

    // 특정 메뉴 옵션 리스트 조회
    List<StoreMenuOption> findOptionsByMenuId(long menuId);

    // 개별 옵션 조회
    StoreMenuOption findOptionById(@Param("menuId")long menuId, @Param("optionId") long optionId);

    // 개별 옵션 삭제
    void deleteOptionById(long optionId);

    // 옵션 그룹 삭제
    void deleteOptionGroupById(long optionGroupId);

    // 요청된 옵션 ID가 존재하는지 확인
    boolean existsOptionById(long optionId);

    // 요청된 그룹 ID가 존재하는지 확인
    boolean existsOptionGroupId(long menuId, long optionGroupId);

}
