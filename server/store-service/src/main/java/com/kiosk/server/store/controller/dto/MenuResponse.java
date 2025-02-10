package com.kiosk.server.store.controller.dto;

public record MenuResponse(long menuId, String menuName, String menuRecipe, int menuPrice, String image) {
}
