package com.kiosk.server.order.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OutboxOption {
    long orderMenuOptionId;
    long orderId;
    long optionValueId;
    long optionCount;
}
