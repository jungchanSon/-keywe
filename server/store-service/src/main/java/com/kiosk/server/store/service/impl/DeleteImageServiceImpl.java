package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuImage;
import com.kiosk.server.store.service.DeleteImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteImageServiceImpl implements DeleteImageService {

    private final MenuImageRepository menuImageRepository;

    @Override
    public void doService(long userId, long menuId) {

        // 기존 이미지 정보 조회
        MenuImage exImage = menuImageRepository.findById(menuId);
        if (exImage == null) {
            return;
        }
        if (exImage.getUserId() != userId) {
            throw new UnauthorizedException("Mismatched userId");
        }

        // DB에서 이미지 삭제
        menuImageRepository.deleteById(menuId);
    }
}
