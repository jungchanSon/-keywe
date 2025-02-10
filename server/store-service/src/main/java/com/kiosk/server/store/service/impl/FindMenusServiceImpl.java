package com.kiosk.server.store.service.impl;

import com.kiosk.server.store.controller.dto.MenuDetailResponse;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.FindMenusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FindMenusServiceImpl implements FindMenusService {

    private final MenuRepository menuRepository;
    private final MenuImageRepository menuImageRepository;

    @Override
    public List<MenuDetailResponse> doService(long userId, Long categoryId) {
        // 메뉴 리스트 가져오기 (카테고리 ID가 없으면 전체 조회)
        List<StoreMenu> menuList = (categoryId == null)
                ? menuRepository.findList(userId)
                : menuRepository.findByCategory(userId, categoryId);

        if (menuList.isEmpty()) {
            return Collections.emptyList();
        }

        // 메뉴 ID 리스트 추출
        List<Long> menuIds = new ArrayList<>();
        for (StoreMenu menu : menuList) {
            menuIds.add(menu.getMenuId());
        }

        // 이미지 데이터 조회 (Base64 인코딩)
        Map<Long, String> imageMap = getMenuImageMap(userId, menuIds);

        // DTO 변환
        List<MenuDetailResponse> menuResponse = new ArrayList<>();
        for (StoreMenu menu : menuList) {
            String imageBase64 = imageMap.get(menu.getMenuId());

            menuResponse.add(new MenuDetailResponse(
                    menu.getMenuId(),
                    menu.getMenuName(),
                    menu.getMenuDesc(),
                    menu.getMenuPrice(),
                    imageBase64,
                    null
            ));
        }

        return menuResponse;
    }

    private Map<Long, String> getMenuImageMap(long userId, List<Long> menuIds) {
        Map<Long, String> imageMap = new HashMap<>();
        for (Long menuId : menuIds) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("menuId", menuId);

            byte[] imageBytes = (byte[]) menuImageRepository.findImageBytesById(params);

            if (imageBytes != null && imageBytes.length > 0) {
                imageMap.put(menuId, Base64.getEncoder().encodeToString(imageBytes));
            } else {
                imageMap.put(menuId, null);
            }
        }
        return imageMap;
    }

}
