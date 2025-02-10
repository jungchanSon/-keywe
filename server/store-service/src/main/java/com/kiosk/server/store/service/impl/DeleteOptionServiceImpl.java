package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.controller.dto.MenuOptionRequest;
import com.kiosk.server.store.domain.MenuOptionRepository;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.domain.StoreMenuOption;
import com.kiosk.server.store.service.DeleteOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteOptionServiceImpl implements DeleteOptionService {

    private final MenuRepository menuRepository;
    private final MenuOptionRepository optionRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void doService(long userId, long menuId, long optionId, MenuOptionRequest request) {
        // 옵션 존재 여부 확인
        StoreMenuOption option = optionRepository.findByOptionId(menuId, optionId);
        if (option == null) {
            throw new EntityNotFoundException("Invalid option");
        }

        // 옵션이 해당 메뉴에 속해있는지 확인
        if (menuId != option.getMenuId()) {
            throw new UnauthorizedException("You do not have permission to delete this option");
        }

        // 메뉴 존재 여부 확인
        StoreMenu menu = menuRepository.findById(userId, menuId);
        if(menu == null) {
            throw new EntityNotFoundException("Invalid menu");
        }

        // 유저 권한 확인
        if(userId != menu.getUserId()) {
            throw new UnauthorizedException("You do not have permission to delete this option");
        }

        if(request.optionGroupId() == null){
            optionRepository.deleteByOptionId(optionId); // 개별 옵션 삭제
        } else {
            optionRepository.deleteOptionGroupById(request.optionGroupId());
        }

    }
}
