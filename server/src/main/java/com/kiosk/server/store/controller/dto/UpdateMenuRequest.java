package com.kiosk.server.store.controller.dto;

public record UpdateMenuRequest(String menuName, String menuDescription, int menuPrice, String menuCategoryName) {
}
