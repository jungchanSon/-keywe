package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.BadRequestException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreMenuCategory {

    private Integer categoryId; // mybatis에서 자동생성
    private String categoryName;
    private LocalDateTime createdAt;

    public StoreMenuCategory() {}

    private StoreMenuCategory(String categoryName, LocalDateTime createdAt) {
        this.categoryName = categoryName;
        this.createdAt = createdAt;
    }

    public static StoreMenuCategory create(String categoryName) {
        return new StoreMenuCategory(categoryName, LocalDateTime.now());
    }

    public void setCategoryId(Integer categoryId) {
        if (this.categoryId != null) {
            throw new BadRequestException("Category id already set");
        }
        this.categoryId = categoryId;
    }

}
