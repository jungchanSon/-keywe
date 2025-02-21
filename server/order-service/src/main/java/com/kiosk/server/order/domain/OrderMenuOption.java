package com.kiosk.server.order.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMenuOption {
    private long orderMenuOptionId;
    private long orderId;
    private long orderMenuId;
    private long optionValueId;
    private long price;
    private long optionCount;
}
