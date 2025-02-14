package com.kiosk.server.image.domain;

import com.kiosk.server.common.util.IdUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Image {
    private long imageId;
    private long userId;
    private long targetId;  // 메뉴 ID 또는 유저 ID
    private byte[] imageBytes;
    private LocalDateTime createdAt;

    private Image(long userId, long targetId, byte[] imageBytes) {
        this.imageId = IdUtil.create();
        this.userId = userId;
        this.targetId = targetId;
        this.imageBytes = imageBytes;
        this.createdAt = LocalDateTime.now();
    }

    public static Image create(long userId, long targetId, byte[] imageBytes) {
        return new Image(userId, targetId, imageBytes);
    }
}
