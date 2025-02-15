package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.image.service.UploadImageService;
import com.kiosk.server.store.controller.dto.CreateMenuRequest;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.CategoryRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.CreateMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateMenuServiceImpl implements CreateMenuService {

    private final MenuRepository menuRepository;
    private final OptionServiceImpl optionService;
    private final CategoryRepository categoryRepository;
    private final UploadImageService uploadImageService;

    @Override
    public CreateMenuResponse doService(long userId, CreateMenuRequest request, MultipartFile image) {
        log.info("CreateMenuService: userId={}, menuName={}, categoryId={}", userId, request.menuName(), request.menuCategoryId());

        if (!categoryRepository.existsById(request.menuCategoryId())) {
            log.warn("존재하지 않는 카테고리 - userId={}, categoryId={}", userId, request.menuCategoryId());
            throw new BadRequestException("존재하지 않는 카테고리입니다.");
        }

        // 메뉴 생성
        StoreMenu menu = StoreMenu.create(
            userId,
            request.menuCategoryId(),
            request.menuName(),
            request.menuDescription(),
            request.menuRecipe(),
            request.menuPrice(),
            request.options());

        menuRepository.insert(menu);
        long menuId = menu.getMenuId();
        log.info("메뉴 생성 완료 - userId={}, menuId={}, menuName={}", userId, menuId, menu.getMenuName());

        // 옵션 존재하면 옵션 저장
        List<OptionGroupResponse> optionGroups = optionService.saveOptionsAndGetResponse(menu.getOptions());

        // 이미지 존재하면 이미지 저장
        if (image != null && !image.isEmpty()) {
            uploadImageService.doService(userId, menuId, image, "menu");
            log.info("이미지 업로드 완료 - userId={}, menuId={}", userId, menuId);
        }

        return new CreateMenuResponse(menuId, menu.getMenuName(), menu.getMenuPrice(), optionGroups);
    }
}
