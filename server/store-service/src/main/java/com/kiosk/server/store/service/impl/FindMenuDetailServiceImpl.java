package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.MenuDetailResponse;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.FindMenuDetailService;
import com.kiosk.server.store.util.OptionServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FindMenuDetailServiceImpl implements FindMenuDetailService {

    private final MenuRepository menuRepository;
    private final MenuImageRepository menuImageRepository;
    private final OptionServiceUtil optionServiceUtil;

    @Override
    public MenuDetailResponse doService(long userId, long menuId) {
        // 메뉴 기본 정보 조회
        StoreMenu menu = menuRepository.findDetailById(userId, menuId);
        if (menu == null) {
            throw new EntityNotFoundException("Menu not found");
        }

        // 메뉴 이미지 조회 & Base64 변환
        Map<String, Object> params = new HashMap<>();
        params.put("menuId", menuId);
        params.put("userId", userId);

        byte[] imageBytes = (byte[]) menuImageRepository.findImageBytesById(params);
        String imageBase64 = (imageBytes != null && imageBytes.length > 0)
                ? "data:image/png;base64," + Base64.getUrlEncoder().withoutPadding().encodeToString(imageBytes)
                : null;

        // 메뉴 옵션 조회 및 그룹화
        List<OptionGroupResponse> optionGroups = optionServiceUtil.getUpdatedOptionGroups(menuId);

        // DTO 변환 후 반환
        return new MenuDetailResponse(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuDesc(),
                menu.getMenuPrice(),
                imageBase64,
                optionGroups
        );
    }
}
