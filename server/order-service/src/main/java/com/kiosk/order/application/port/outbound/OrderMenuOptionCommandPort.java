package com.kiosk.order.application.port.outbound;

import com.kiosk.order.domain.OrderMenuOption;

public interface OrderMenuOptionCommandPort {
    long create(OrderMenuOption orderMenuOption);
}
