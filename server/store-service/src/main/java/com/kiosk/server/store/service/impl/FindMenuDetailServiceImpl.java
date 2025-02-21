package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.image.repository.MenuImageRepository;
import com.kiosk.server.store.controller.dto.MenuDetailResponse;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.FindMenuDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindMenuDetailServiceImpl implements FindMenuDetailService {

    private final MenuRepository menuRepository;
    private final MenuImageRepository imageRepository;
    private final OptionServiceImpl optionService;

    @Override
    public MenuDetailResponse doService(long userId, long menuId) {
        log.info("FindMenuDetailService: userId={}, menuId={}", userId, menuId);

        // 메뉴 기본 정보 조회
        StoreMenu menu = menuRepository.findById(userId, menuId);
        if (menu == null) {
            log.warn("메뉴 조회 실패 - userId={}, menuId={}", userId, menuId);
            throw new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다. 메뉴 정보를 다시 확인해 주세요.");
        }

        // 메뉴 이미지 조회 & Base64 변환
        Map<String, Object> params = new HashMap<>();
        params.put("targetId", menuId);
        params.put("userId", userId);

        byte[] imageBytes = (byte[]) imageRepository.findImageBytesById(params);
        String image = (imageBytes != null && imageBytes.length > 0)
                ? "data:image/png;base64," + Base64.getUrlEncoder().withoutPadding().encodeToString(imageBytes)
                : null;

        // 메뉴 옵션 조회 및 그룹화
        List<OptionGroupResponse> optionGroups = optionService.getUpdatedOptionGroups(menuId);

        log.info("메뉴 상세 조회 완료 - userId={}, menuId={}, imageExists={}, optionGroupCount={}",
                userId, menuId, image != null, optionGroups.size());

        // DTO 변환 후 반환
        return MenuDetailResponse.of(menu, image, optionGroups);
    }
}
