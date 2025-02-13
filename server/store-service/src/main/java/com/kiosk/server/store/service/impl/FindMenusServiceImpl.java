package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.controller.dto.MenuResponse;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.FindMenusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindMenusServiceImpl implements FindMenusService {

    private final MenuRepository menuRepository;
    private final MenuImageRepository imageRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<MenuResponse> doService(long userId, Long categoryId) {
        log.info("FindMenusService: userId={}, categoryId={}", userId, categoryId);

        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            log.warn("존재하지 않는 카테고리 요청 - userId={}, categoryId={}", userId, categoryId);
            throw new BadRequestException("존재하지 않는 카테고리입니다.");
        }

        // 메뉴 리스트 가져오기 (카테고리 ID가 없으면 전체 조회)
        List<StoreMenu> menuList = (categoryId == null)
                ? menuRepository.findAll(userId)
                : menuRepository.findByCategory(userId, categoryId);

        if (menuList.isEmpty()) {
            log.info("조회된 메뉴 없음 - userId={}, categoryId={}", userId, categoryId);
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
        List<MenuResponse> menuResponse = new ArrayList<>();
        for (StoreMenu menu : menuList) {
            String image = imageMap.get(menu.getMenuId());

            menuResponse.add(new MenuResponse(
                    menu.getMenuId(),
                    menu.getMenuName(),
                    menu.getMenuRecipe(),
                    menu.getMenuPrice(),
                    image
            ));
        }

        log.info("메뉴 조회 완료 - userId={}, categoryId={}, menuCount={}", userId, categoryId, menuResponse.size());

        return menuResponse;
    }

    private Map<Long, String> getMenuImageMap(long userId, List<Long> menuIds) {
        Map<Long, String> imageMap = new HashMap<>();
        for (Long menuId : menuIds) {
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("menuId", menuId);

            byte[] imageBytes = (byte[]) imageRepository.findImageBytesById(params);

            if (imageBytes != null && imageBytes.length > 0) {
                imageMap.put(menuId, Base64.getEncoder().encodeToString(imageBytes));
            } else {
                imageMap.put(menuId, null);
            }
        }
        return imageMap;
    }
}
