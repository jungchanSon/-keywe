package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.BadRequestException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Images {

    private int imageId; // mybatis에서 자동생성
    private String imagePath;
    private LocalDateTime createdAt;

    public Images(){}

    public Images(String imagePath) {
        if (imagePath == null) {
            throw new BadRequestException("Invalid image path");
        }
        this.imagePath = imagePath;
        this.createdAt = LocalDateTime.now();
    }

    public void setImageId(Integer imageId) {
        if (this.imageId > 0) {
            throw new BadRequestException("Image id already set");
        }
        this.imageId = imageId;
    }
}
