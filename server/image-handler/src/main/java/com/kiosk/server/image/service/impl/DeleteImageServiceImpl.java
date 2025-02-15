package com.kiosk.server.image.service.impl;

import com.kiosk.server.common.exception.custom.EntityNotFoundException;
import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.image.domain.Image;
import com.kiosk.server.image.repository.ImageRepository;
import com.kiosk.server.image.repository.MenuImageRepository;
import com.kiosk.server.image.repository.ProfileImageRepository;
import com.kiosk.server.image.service.DeleteImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteImageServiceImpl implements DeleteImageService {

    private final MenuImageRepository menuImageRepository;
    private final ProfileImageRepository profileImageRepository;

    @Override
    public void doService(long userId, long targetId, String type) {
        log.info("DeleteImageService: userId={}, targetId={}, type={}", userId, targetId, type);

        ImageRepository repository = getRepositoryByType(type);

        Image exImage = repository.findByTargetId(targetId);
        if (exImage == null) {
            log.warn("삭제할 이미지 없음 - userId={}, targetId={}", userId, targetId);
            return;
        }
        if (exImage.getUserId() != userId) {
            log.warn("이미지 삭제 권한 없음 - userId={}, targetId={}", userId, targetId);
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        // DB에서 이미지 삭제
        repository.deleteById(targetId);
        log.info("이미지 삭제 완료 - userId={}, targetId={}", userId, targetId);

    }

    private ImageRepository getRepositoryByType(String type) {
        if("menu".equals(type)){
            return menuImageRepository;
        } else if("profile".equals(type)){
            return profileImageRepository;
        } else {
            log.warn("유효하지 않은 이미지 요청 - type={}", type);
            throw new EntityNotFoundException("유효하지 않은 이미지 요청: "+type);
        }
    }
}
