package com.kiosk.server.order.application.service.order.vo;

import java.util.List;

public class OrderVO {

    public record save(
            long profileId,
            long totalPrice,
            long shopId,
            String phoneNumber,
            List<OrderMenu> menuList
    ) { }

    public record OrderMenu(
            long menuId,
            long menuCount,
            List<OrderMenuOption> optionList
    ) { }

    public record OrderMenuOption(
            long optionValueId,
            long optionCount
    ) { }
}
