package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.DeleteMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteMenuServiceImpl implements DeleteMenuService {

    private final MenuRepository menuRepository;

    @Override
    public void doService(long userId, long menuId) {
        // 메뉴 존재 여부 확인
        StoreMenu menu = menuRepository.findById(userId, menuId);
        if (menu == null) {
            throw new EntityNotFoundException("Menu Not found");
        }

        // 유저 권한 확인
        if (userId != menu.getUserId()) {
            throw new UnauthorizedException("You do not have permission to delete this menu");
        }

        // 메뉴 삭제 (옵션, 이미지 ON DELETE CASCADE로 자동 삭제)
        menuRepository.deleteById(menuId);
    }
}
