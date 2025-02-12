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
            throw new BadRequestException("유효하지 않은 사용자 ID입니다. 올바른 사용자 정보를 입력해 주세요.");
        }
        if (imageBytes == null) {
            throw new BadRequestException("이미지 데이터가 누락되었습니다. 올바른 이미지를 제공해 주세요.");
        }
        if (menuId <= 0) {
            throw new BadRequestException("유효하지 않은 메뉴 ID입니다. 올바른 메뉴 정보를 입력해 주세요.");
        }
    }

}
