package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.store.controller.dto.UpdateMenuRequest;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.UpdateMenuService;
import com.kiosk.server.store.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateMenuServiceImpl implements UpdateMenuService {

    private final MenuRepository menuRepository;
    private final UploadImageService uploadImageService;
    private final MenuImageRepository menuImageRepository;

    @Override
    public void doService(long userId, long menuId, UpdateMenuRequest updateMenu, MultipartFile image) {

        // 기존 메뉴 조회
        StoreMenu exMenu = getStoreMenu(userId, menuId);

        // 카테고리 ID로 변환
        Long categoryId = getCategoryId(userId, updateMenu);

        // 업데이트할 데이터 매핑
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("menuId", menuId);
        updateParams.put("userId", userId);
        updateParams.put("categoryId", categoryId);

        // 메뉴명, 설명 설정 (새로운 값이 없으면 기존 값 반환)
        updateParams.put("menuName", getUpdatedValue(updateMenu.menuName(), exMenu.getMenuName()));
        updateParams.put("menuDesc", getUpdatedValue(updateMenu.menuDescription(), exMenu.getMenuDescription()));

        // 메뉴 가격 설정
        int menuPrice = updateMenu.menuPrice();
        if (menuPrice < 0) {
            menuPrice = exMenu.getMenuPrice();
        }
        updateParams.put("menuPrice", menuPrice);

        // 메뉴 업데이트 실행
        menuRepository.update(updateParams);

        // 이미지 존재하면 이미지 저장
        if (image != null && !image.isEmpty()) {
            menuImageRepository.deleteById(menuId);
            uploadImageService.doService(userId, menuId, image);
        }
    }

    private StoreMenu getStoreMenu(long userId, long menuId) {
        StoreMenu exMenu = menuRepository.findById(userId, menuId);
        if (exMenu == null) {
            throw new EntityNotFoundException("Menu not found");
        }
        return exMenu;
    }

    private Long getCategoryId(long userId, UpdateMenuRequest updateMenu) {
        Long categoryId = menuRepository.findCategoryIdByName(userId, updateMenu.menuCategoryName());
        if (categoryId == null) {
            throw new EntityNotFoundException("No such category");
        }
        return categoryId;
    }

    private String getUpdatedValue(String newValue, String existingValue) {
        return StringUtils.hasLength(newValue) ? newValue : existingValue;
    }
}
