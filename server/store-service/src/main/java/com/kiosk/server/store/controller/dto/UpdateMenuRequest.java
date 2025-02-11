package com.kiosk.server.store.controller.dto;

public record UpdateMenuRequest(String menuName, String menuDescription, String menuRecipe, int menuPrice, String menuCategoryName) {
}
