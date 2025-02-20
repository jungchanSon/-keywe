package com.kiosk.server.infra.kafka.event;

import com.kiosk.server.infra.rdb.entity.outbox.OutBoxStatus;

import java.util.List;

public class OrderEvent {

    public record OrderCreatedEvent (
            long orderId,
            long shopId,
            long totalPrice,
            Long customerId,
            List<Menu> menuList
    ) { }

    public record Menu (
            long menuId,
            long menuCount,
            List<Option> optionList
    ) { }

    public record Option (
            long optionValueId,
            long optionCount
    ) { }
}
