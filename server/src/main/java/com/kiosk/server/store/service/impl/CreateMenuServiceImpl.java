package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.controller.dto.CreateMenuRequest;
import com.kiosk.server.store.controller.dto.CreateMenuResponse;
import com.kiosk.server.store.controller.dto.OptionGroupResponse;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.CreateMenuService;
import com.kiosk.server.store.service.UploadImageService;
import com.kiosk.server.store.util.OptionServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateMenuServiceImpl implements CreateMenuService {

    private final MenuRepository menuRepository;
    private final OptionServiceUtil optionServiceUtil;
    private final UploadImageService uploadImageService;

    @Override
    public CreateMenuResponse doService(long userId, CreateMenuRequest request, MultipartFile image) {

        // 카테고리 ID로 변환
        Long categoryId = menuRepository.findCategoryIdByName(userId, request.menuCategoryName());
        if (categoryId == null) {
            throw new BadRequestException("No such category");
        }
        // 메뉴 생성
        StoreMenu menu = StoreMenu.create(userId, categoryId, request);
        menuRepository.insert(menu);
        long menuId = menu.getMenuId();

        // 옵션 존재하면 옵션 저장
        List<OptionGroupResponse> optionGroups = optionServiceUtil.saveOptionsAndGetResponse(menu.getOptions());

        // 이미지 존재하면 이미지 저장
        if(image != null && !image.isEmpty()) {
           uploadImageService.doService(userId, menuId, image);
        }

        return new CreateMenuResponse(menuId, optionGroups);
    }
}
