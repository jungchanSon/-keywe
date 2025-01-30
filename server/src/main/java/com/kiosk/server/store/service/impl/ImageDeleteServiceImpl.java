package com.kiosk.server.store.service.impl;

import com.kiosk.server.store.domain.ImageRepository;
import com.kiosk.server.store.domain.Images;
import com.kiosk.server.store.service.ImageDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ImageDeleteServiceImpl implements ImageDeleteService {

    private final ImageRepository imageRepository;

    @Override
    public void doService(int imageId) {

        if (imageId == 0){
            return; // 이미지 없는 경우 삭제하지 않음
        }

        // 기존 이미지 정보 조회
        Images exImage = imageRepository.findImageById(imageId);
        if (exImage == null){
            return;
        }

        // 파일 삭제
        try{
            Path imagePath = Paths.get(exImage.getImagePath());
            Files.deleteIfExists(imagePath);
        } catch (Exception e){
            throw new RuntimeException("기존 이미지 삭제 실패");
        }

        // DB에서 삭제
        imageRepository.deleteImageById(imageId);
    }
}
