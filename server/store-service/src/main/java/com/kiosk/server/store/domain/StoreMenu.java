package com.kiosk.server.store.domain;

import com.kiosk.server.common.exception.custom.BadRequestException;
import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.store.controller.dto.MenuOptionData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreMenu {
    private long menuId;
    private long userId;
    private long categoryId;
    private String menuName;
    private String menuDescription;
    private int menuPrice;
    private List<StoreMenuOption> options;
    private LocalDateTime createdAt;

    public static StoreMenu create(long userId, long categoryId, String menuName, String menuDescription, int menuPrice, List<MenuOptionData> optionList) {
        validateInputData(userId, categoryId, menuName, menuPrice);
        long menuId = IdUtil.create();
        List<StoreMenuOption> options = StoreMenuOption.createFromList(optionList, menuId);
        StoreMenu menu = new StoreMenu();
        menu.menuId = menuId;
        menu.userId = userId;
        menu.categoryId = categoryId;
        menu.menuName = menuName;
        menu.menuDescription = menuDescription;
        menu.menuPrice = menuPrice;
        menu.options = options;
        menu.createdAt = LocalDateTime.now();
        return menu;
    }

    private static void validateInputData(long userId, long categoryId, String name, int price) {
        if (userId <= 0) {
            throw new BadRequestException("Invalid user id");
        }
        if (categoryId <= 0) {
            throw new BadRequestException("Invalid category id");
        }
        if (!StringUtils.hasLength(name)) {
            throw new BadRequestException("Name cannot be empty");
        }
        if (price < 0) {
            throw new BadRequestException("Invalid menu price");
        }
    }

}
