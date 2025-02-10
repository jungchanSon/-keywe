package com.kiosk.server.store.controller.dto;

import com.kiosk.server.store.domain.StoreMenu;

import java.util.List;

public record MenuDetailResponse(long menuId, String menuName, String menuDesc, int menuPrice, String image,
                                 List<OptionGroupResponse> optionsGroups) {

    public static MenuDetailResponse of(StoreMenu menu, String imageBase64, List<OptionGroupResponse> optionsGroups) {
        return new MenuDetailResponse(
                menu.getMenuId(),
                menu.getMenuName(),
                menu.getMenuDescription(),
                menu.getMenuPrice(),
                imageBase64,
                optionsGroups
        );
    }
}
