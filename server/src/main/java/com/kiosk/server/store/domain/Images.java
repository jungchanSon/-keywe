package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.util.IdUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Images {

    private long imageId;
    private long menuId;
    private long userId;
    private byte[] imageBytes;
    private LocalDateTime createdAt;

    public static Images create(long userId, long menuId, byte[] imageBytes) {
        validateInputData(userId, menuId, imageBytes);
        Images images = new Images();
        images.imageId = IdUtil.create();
        images.userId = userId;
        images.menuId = menuId;
        images.imageBytes = imageBytes;
        images.createdAt = LocalDateTime.now();
        return images;
    }

    private static void validateInputData(long userId, long menuId, byte[] imageBytes) {
        if (userId <= 0) {
            throw new BadRequestException("Invalid user id");
        }
        if (imageBytes == null) {
            throw new BadRequestException("Invalid image");
        }
        if(menuId <= 0) {
            throw new BadRequestException("Invalid menu id");
        }
    }

}
