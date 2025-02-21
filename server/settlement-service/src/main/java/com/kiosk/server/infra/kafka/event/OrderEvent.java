package com.kiosk.server.infra.kafka.event;

import java.util.List;

public class OrderEvent {

    public record CreatedEvent(
            Long orderId,
            Long shopId,
            Long totalPrice,
            Long customerId,
            List<Menu> menuList
    ) { }

    public record Menu (
            Long menuId,
            Long menuCount,
            List<Option> optionList
    ) { }

    public record Option (
            Long optionValueId,
            Long optionCount
    ) { }
}
