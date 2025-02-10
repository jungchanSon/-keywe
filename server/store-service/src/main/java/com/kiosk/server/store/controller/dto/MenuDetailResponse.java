package com.kiosk.server.store.controller.dto;

import com.kiosk.server.store.domain.StoreMenu;

import java.util.List;

public record MenuDetailResponse(long menuId, String menuName, String menuDescription, String menuRecipe, int menuPrice,
                                 String imageBase64,
                                 List<OptionGroupResponse> options) {
    public static MenuDetailResponse of(StoreMenu menu, String imageBase64, List<OptionGroupResponse> optionsGroups) {
        return new MenuDetailResponse(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuDescription(),
                menu.getMenuRecipe(),
                menu.getMenuPrice(),
                imageBase64,
                optionsGroups
        );
    }
}
