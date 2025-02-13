package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.MenuImage;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.service.DeleteImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteImageServiceImpl implements DeleteImageService {

    private final MenuImageRepository menuImageRepository;

    @Override
    public void doService(long userId, long menuId) {
        log.info("DeleteImageService: userId={}, menuId={}", userId, menuId);

        // 기존 이미지 정보 조회
        MenuImage exImage = menuImageRepository.findByMenuId(menuId);
        if (exImage == null) {
            log.warn("삭제할 이미지 없음 - userId={}, menuId={}", userId, menuId);
            return;
        }
        if (exImage.getUserId() != userId) {
            log.warn("이미지 삭제 권한 없음 - userId={}, menuId={}", userId, menuId);
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        // DB에서 이미지 삭제
        menuImageRepository.deleteById(menuId);
        log.info("이미지 삭제 완료 - userId={}, menuId={}", userId, menuId);
    }
}
