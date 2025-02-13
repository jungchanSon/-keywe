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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteOptionServiceImpl implements DeleteOptionService {

    private final MenuRepository menuRepository;
    private final MenuOptionRepository optionRepository;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void doService(long userId, long menuId, long optionId, MenuOptionRequest request) {
        log.info("DeleteOptionService: userId={}, menuId={}, optionId={}, requestOptionId={}",
                userId, menuId, optionId, request.optionId());

        // 옵션 존재 여부 확인
        StoreMenuOption option = optionRepository.findByOptionId(menuId, optionId);
        if (option == null) {
            log.warn("삭제할 옵션 없음 - userId={}, menuId={}, optionId={}", userId, menuId, optionId);
            throw new EntityNotFoundException("삭제하려는 옵션을 찾을 수 없습니다. 옵션 정보를 확인해 주세요.");
        }

        // 옵션이 해당 메뉴에 속해있는지 확인
        if (menuId != option.getMenuId()) {
            log.warn("옵션 삭제 권한 없음 - userId={}, menuId={}, optionId={}", userId, menuId, optionId);
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

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

        if (request.optionId() == null) {
            optionRepository.deleteByOptionId(optionId); // 개별 옵션 삭제
            log.info("개별 옵션 삭제 완료 - userId={}, menuId={}, optionId={}", userId, menuId, optionId);
        } else {
            optionRepository.deleteOptionGroupById(request.optionId());
            log.info("옵션 그룹 삭제 완료 - userId={}, menuId={}, groupId={}", userId, menuId, request.optionId());
        }
    }
}
