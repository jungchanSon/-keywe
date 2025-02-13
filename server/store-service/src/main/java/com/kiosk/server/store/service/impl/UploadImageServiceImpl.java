package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuImage;
import com.kiosk.server.store.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadImageServiceImpl implements UploadImageService {

    private final MenuImageRepository menuImageRepository;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    @Override
    public void doService(long userId, long menuId, MultipartFile file) {
        log.info("UploadImageService: userId={}, menuId={}, fileName={}", userId, menuId, file != null ? file.getOriginalFilename() : "null");

        // 파일 존재 여부 확인
        if (file == null || file.isEmpty()) {
            log.warn("이미지 업로드 실패 - 파일 없음, userId={}, menuId={}", userId, menuId);
            throw new BadRequestException("파일이 선택되지 않았습니다. 업로드할 파일을 선택해 주세요.");
        }

        // 파일 확장자 검증
        validateFileExtension(file.getOriginalFilename());

        try {
            // 파일을 바이트 배열로 변환
            byte[] imageBytes = file.getBytes();

            // 이미지 정보 DB 저장
            MenuImage image = MenuImage.create(userId, menuId, imageBytes);
            menuImageRepository.insert(image);

            log.info("이미지 업로드 완료 - userId={}, menuId={}, fileSize={} bytes", userId, menuId, imageBytes.length);
        } catch (IOException e) {
            log.error("이미지 파일 처리 중 오류 발생 - userId={}, menuId={}, error={}", userId, menuId, e.getMessage(), e);
            throw new RuntimeException("이미지 파일 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
        }
    }

    private void validateFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            log.warn("파일 확장자 검증 실패 - 파일명 없음 또는 형식 오류");
            throw new BadRequestException("파일 형식이 올바르지 않습니다. 올바른 파일을 선택해 주세요.");
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            log.warn("지원하지 않는 파일 형식 - fileName={}, extension={}", fileName, extension);
            throw new BadRequestException("지원하지 않는 파일 형식입니다. 업로드 가능한 형식: " + extension);
        }
    }
}
