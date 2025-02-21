package com.kiosk.server.order.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OutboxMenu {
    long orderMenuId;
    long orderId;
    long menuId;
    long menuCount;
}
