package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.store.domain.ImageRepository;
import com.kiosk.server.store.domain.Images;
import com.kiosk.server.store.service.ImageDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageDeleteServiceImpl implements ImageDeleteService {

    private final ImageRepository imageRepository;

    @Override
    public void doService(long userId, int imageId) {

        if (imageId == 0) {
            return; // 이미지 없는 경우 삭제하지 않음
        }

        // 기존 이미지 정보 조회
        Images exImage = imageRepository.findImageById(imageId);
        if (exImage == null) {
            return;
        }
        if (exImage.getUserId() != userId) {
            throw new UnauthorizedException("Mismatched userId");
        }

        // DB에서 이미지 삭제
        imageRepository.deleteImageById(imageId);
    }
}
