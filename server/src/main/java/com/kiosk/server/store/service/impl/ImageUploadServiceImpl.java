package com.kiosk.server.store.service.impl;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.store.domain.ImageRepository;
import com.kiosk.server.store.domain.Images;
import com.kiosk.server.store.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {

    private final ImageRepository imageRepository;

    @Value("${upload.path}")  // application.yml에서 설정
    private String uploadPath;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    @Override
    public Images doService(MultipartFile file) {

        // 파일 존재 여부 확인
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("no file selected");
        }

        // 파일 저장 후 저장 경로 반환
        String imagePath = saveFile(file);

        // 이미지 정보 DB저장
        Images image = new Images(imagePath);
        imageRepository.insertImage(image);
        int imageId = image.getImageId();

        return imageRepository.findImageById(imageId);
    }

    private String saveFile(MultipartFile file) {

        try {
            // 파일 확장자 검증
            validateFileExtension(file.getOriginalFilename());

            // 고유한 파일명 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path filePath = uploadDir.resolve(fileName);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // 파일명에 상대경로가 포함되어 있는지 확인
            if (fileName.contains("..")) {
                throw new BadRequestException("Invalid file name.");
            }

            // 파일 저장
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toString();
        } catch (Exception e) {
            throw new BadRequestException("Failed to save the file");
        }
    }

    private void validateFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new BadRequestException("Invalid file format");
        }
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Unsupported file format" + extension);
        }
    }
}