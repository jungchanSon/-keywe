package com.kiosk.order.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Order {
    private long orderId;
    private long shopId;
    private long phoneNumber;
    private List<OrderMenu> orderMenuList;
}
