package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.BadRequestException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Images {

    private int imageId; // mybatis에서 자동생성
    private long userId;
    private byte[] imageBytes;
    private LocalDateTime createdAt;


    public Images(){}

    public Images(long userId, byte[] imageBytes) {
        if (userId <= 0) {
            throw new BadRequestException("Invalid user id");
        }
        if (imageBytes == null) {
            throw new BadRequestException("Invalid image path");
        }
        this.userId = userId;
        this.imageBytes = imageBytes;
        this.createdAt = LocalDateTime.now();
    }

    public void setImageId(Integer imageId) {
        if (this.imageId > 0) {
            throw new BadRequestException("Image id already set");
        }
        this.imageId = imageId;
    }
}
