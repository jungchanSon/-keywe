package com.kiosk.server.store.service;

import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.StoreMenuOption;

import java.util.List;

public interface OptionService {

    // 메뉴와 옵션 존재 여부 검증
    void validateMenuAndOption(long userId, long menuId, Long optionId);
    // 옵션 리스트 저장하고 옵션 그룹 응답 생성
    List<OptionGroupResponse> saveOptionsAndGetResponse(List<StoreMenuOption> options);

    // 단일 옵션 추가 시 사용
    List<OptionGroupResponse> addOptionAndGetResponse(StoreMenuOption option);

    // 최신 옵션 목록을 조회하고 옵션 그룹 응답을 생성
    List<OptionGroupResponse> getUpdatedOptionGroups(long menuId);
}
