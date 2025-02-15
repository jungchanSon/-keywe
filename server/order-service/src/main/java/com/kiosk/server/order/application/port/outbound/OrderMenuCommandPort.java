package com.kiosk.server.order.application.port.outbound;

import com.kiosk.server.order.domain.OrderMenu;

public interface OrderMenuCommandPort {
    long create(OrderMenu orderMenu);
}
