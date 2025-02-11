package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.util.IdUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MenuImage {

    private long imageId;
    private long menuId;
    private long userId;
    private byte[] imageBytes;
    private LocalDateTime createdAt;

    public static MenuImage create(long userId, long menuId, byte[] imageBytes) {
        validateInputData(userId, menuId, imageBytes);
        MenuImage menuImage = new MenuImage();
        menuImage.imageId = IdUtil.create();
        menuImage.userId = userId;
        menuImage.menuId = menuId;
        menuImage.imageBytes = imageBytes;
        menuImage.createdAt = LocalDateTime.now();
        return menuImage;
    }

    private static void validateInputData(long userId, long menuId, byte[] imageBytes) {
        if (userId <= 0) {
            throw new BadRequestException("Invalid user id");
        }
        if (imageBytes == null) {
            throw new BadRequestException("Invalid image");
        }
        if (menuId <= 0) {
            throw new BadRequestException("Invalid menu id");
        }
    }

}
