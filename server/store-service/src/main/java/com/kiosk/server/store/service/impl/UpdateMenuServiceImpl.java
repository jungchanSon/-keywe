package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.UpdateMenuRequest;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.UpdateMenuService;
import com.kiosk.server.store.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateMenuServiceImpl implements UpdateMenuService {

    private final MenuRepository menuRepository;
    private final UploadImageService uploadImageService;
    private final MenuImageRepository menuImageRepository;

    @Override
    public void doService(long userId, long menuId, UpdateMenuRequest updateMenu, MultipartFile image) {
        log.info("UpdateMenuService: userId={}, menuId={}, updateMenu={}", userId, menuId, updateMenu);

        // 기존 메뉴 조회
        StoreMenu exMenu = getStoreMenu(userId, menuId);

        if (updateMenu.menuCategoryId() == null) {
            log.warn("메뉴 업데이트 실패 - userId={}, menuId={}, 이유=카테고리 없음", userId, menuId);
            throw new EntityNotFoundException("해당 카테고리를 찾을 수 없습니다. 입력하신 정보를 다시 확인해 주세요.");
        }

        // 업데이트할 데이터 매핑
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("menuId", menuId);
        updateParams.put("userId", userId);
        updateParams.put("categoryId", updateMenu.menuCategoryId());

        // 메뉴명, 설명 설정 (새로운 값이 없으면 기존 값 반환)
        updateParams.put("menuName", getUpdatedValue(updateMenu.menuName(), exMenu.getMenuName()));
        updateParams.put("menuDescription", getUpdatedValue(updateMenu.menuDescription(), exMenu.getMenuDescription()));
        updateParams.put("menuRecipe", getUpdatedValue(updateMenu.menuRecipe(), exMenu.getMenuRecipe()));

        // 메뉴 가격 설정
        int menuPrice = updateMenu.menuPrice();
        if (menuPrice < 0) {
            menuPrice = exMenu.getMenuPrice();
        }
        updateParams.put("menuPrice", menuPrice);

        // 메뉴 업데이트 실행
        menuRepository.update(updateParams);
        log.info("메뉴 업데이트 완료 - userId={}, menuId={}, newCategoryId={}, newPrice={}",
                userId, menuId, updateMenu.menuCategoryId(), menuPrice);

        // 이미지 존재하면 이미지 저장
        if (image != null && !image.isEmpty()) {
            menuImageRepository.deleteById(menuId);
            uploadImageService.doService(userId, menuId, image);
            log.info("메뉴 이미지 업데이트 완료 - userId={}, menuId={}", userId, menuId);
        }
    }

    private StoreMenu getStoreMenu(long userId, long menuId) {
        StoreMenu exMenu = menuRepository.findById(userId, menuId);
        if (exMenu == null) {
            log.warn("메뉴 조회 실패 - userId={}, menuId={}", userId, menuId);
            throw new EntityNotFoundException("해당 메뉴를 찾을 수 없습니다. 입력하신 정보를 다시 확인해 주세요.");
        }
        return exMenu;
    }

    private String getUpdatedValue(String newValue, String existingValue) {
        return StringUtils.hasLength(newValue) ? newValue : existingValue;
    }
}
