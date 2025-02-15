package com.kiosk.server.order.adaptor.inbound.api.dto;

import java.util.List;

public class OrderRequestDTO {

    public record Order(
        String phoneNumber,
        List<MenuItem> menuList
    ) { }

    public record MenuItem (
        long menuId,
        long menuCount,
        List<OptionItem> optionList
    ) { }

    public record OptionItem(
        long optionValueId,
        long optionCount
    ) { }

}
