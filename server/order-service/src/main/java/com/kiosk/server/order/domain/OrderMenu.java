package com.kiosk.server.order.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderMenu {
    private long orderMenuId;
    private long orderId;
    private long menuId;
    private long menuCount;
    List<OrderMenuOption> optionList;
}
