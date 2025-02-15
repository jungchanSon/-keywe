package com.kiosk.server.order.application.port.outbound;

import com.kiosk.server.order.domain.OrderMenuOption;

public interface OrderMenuOptionCommandPort {
    long create(OrderMenuOption orderMenuOption);
}
