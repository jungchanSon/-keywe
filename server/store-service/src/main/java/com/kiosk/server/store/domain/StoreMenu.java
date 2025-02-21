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
    private String menuRecipe;
    private int menuPrice;
    private List<StoreMenuOption> options;
    private LocalDateTime createdAt;

    public static StoreMenu create(long userId, long categoryId, String menuName, String menuDescription, String menuRecipe, int menuPrice, List<MenuOptionData> menuOptions) {
        validateInputData(userId, categoryId, menuName, menuRecipe, menuPrice);
        long menuId = IdUtil.create();
        List<StoreMenuOption> options = StoreMenuOption.createFromList(menuOptions, menuId);
        StoreMenu menu = new StoreMenu();
        menu.menuId = menuId;
        menu.userId = userId;
        menu.categoryId = categoryId;
        menu.menuName = menuName;
        menu.menuDescription = menuDescription;
        menu.menuRecipe = menuRecipe;
        menu.menuPrice = menuPrice;
        menu.options = options;
        menu.createdAt = LocalDateTime.now();
        return menu;
    }

    private static void validateInputData(long userId, long categoryId, String name, String recipe, int price) {
        if (userId <= 0) {
            throw new BadRequestException("유효하지 않은 사용자 ID입니다. 올바른 사용자 정보를 입력해 주세요.");
        }
        if(categoryId <= 0) {
            throw new BadRequestException("유효하지 않은 카테고리 ID입니다. 올바른 카테고리 정보를 입력해 주세요.");
        }
        if (!StringUtils.hasLength(name)) {
            throw new BadRequestException("이름이 입력되지 않았습니다. 이름을 입력해 주세요.");
        }
        if(!StringUtils.hasLength(recipe)) {
            throw new BadRequestException("레시피 내용이 입력되지 않았습니다. 레시피를 입력해 주세요.");
        }
        if (price < 0) {
            throw new BadRequestException("메뉴 가격이 올바르지 않습니다. 올바른 가격을 입력해 주세요.");
        }
    }

}
