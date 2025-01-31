package com.kiosk.server.store.domain;

import com.kiosk.server.common.util.IdUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMenuCategory {

    private Long categoryId;
    private Long userId;
    private String categoryName;
    private LocalDateTime createdAt;

    public static StoreMenuCategory create(Long userId, String categoryName) {
        StoreMenuCategory category = new StoreMenuCategory();
        category.categoryId = IdUtil.create();
        category.userId = userId;
        category.categoryName = categoryName;
        category.createdAt = LocalDateTime.now();
        return category;
    }
}
