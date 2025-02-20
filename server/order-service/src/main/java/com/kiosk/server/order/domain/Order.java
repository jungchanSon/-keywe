package com.kiosk.server.order.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Order {
    private long orderId;
    private Long totalPrice;
    private long shopId;
    private long profileId;
    private long phoneNumber;
    private List<OrderMenu> orderMenuList;
}
