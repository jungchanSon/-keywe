package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.domain.MenuImageRepository;
import com.kiosk.server.store.domain.MenuImage;
import com.kiosk.server.store.service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UploadImageServiceImpl implements UploadImageService {

    private final MenuImageRepository menuImageRepository;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    @Override
    public void doService(long userId, long menuId, MultipartFile file) {

        // 파일 존재 여부 확인
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("No file selected");
        }

        // 파일 확장자 검증
        validateFileExtension(file.getOriginalFilename());

        try {
            // 파일을 바이트 배열로 변환
            byte[] imageBytes = file.getBytes();

            // 이미지 정보 DB 저장
            MenuImage image = MenuImage.create(userId, menuId, imageBytes);
            menuImageRepository.insert(image);

        } catch (IOException e) {
            throw new RuntimeException("Failed to process the image file");
        }
    }

    private void validateFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new BadRequestException("Invalid file format");
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Unsupported file format: " + extension);
        }
    }

}