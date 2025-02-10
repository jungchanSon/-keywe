package com.kiosk.server.store.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

public record CreateMenuRequest(
        String menuName,
        String menuCategoryName,
        String menuDescription,
        String menuRecipe,
        int menuPrice,
        @JsonInclude(JsonInclude.Include.NON_NULL) List<MenuOptionData> options
) {
    // null 값이 들어오면 빈 리스트로 변환하는 생성자 추가
    public CreateMenuRequest {
        options = (options != null) ? options : new ArrayList<>();
    }
}