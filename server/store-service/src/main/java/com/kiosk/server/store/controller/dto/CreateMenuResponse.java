package com.kiosk.server.store.controller.dto;

import java.util.List;

public record CreateMenuResponse(long menuId, String menuName, int menuPrice, List<OptionGroupResponse> options) {
}
