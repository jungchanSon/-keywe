package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.MenuRepository;
import com.kiosk.server.store.domain.StoreMenu;
import com.kiosk.server.store.service.DeleteMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteMenuServiceImpl implements DeleteMenuService {

    private final MenuRepository menuRepository;

    @Override
    public void doService(long userId, long menuId) {
        log.info("DeleteMenuService: userId={}, menuId={}", userId, menuId);

        // 메뉴 존재 여부 확인
        StoreMenu menu = menuRepository.findById(userId, menuId);
        if (menu == null) {
            log.warn("삭제할 메뉴 없음 - userId={}, menuId={}", userId, menuId);
            throw new EntityNotFoundException("삭제하려는 메뉴를 찾을 수 없습니다. 메뉴 정보를 확인해 주세요.");
        }

        // 유저 권한 확인
        if (userId != menu.getUserId()) {
            log.warn("메뉴 삭제 권한 없음 - userId={}, menuId={}", userId, menuId);
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        // 메뉴 삭제 (옵션, 이미지 ON DELETE CASCADE로 자동 삭제)
        menuRepository.deleteById(menuId);
        log.info("메뉴 삭제 완료 - userId={}, menuId={}", userId, menuId);
    }
}
