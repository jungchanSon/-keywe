package com.kiosk.server.store.controller.dto;

import java.util.List;

public record MenuDetailResponse(long menuId, String menuName, String menuDesc, int menuPrice, String image,
                                 List<OptionGroupResponse> optionsGroups) {
}
