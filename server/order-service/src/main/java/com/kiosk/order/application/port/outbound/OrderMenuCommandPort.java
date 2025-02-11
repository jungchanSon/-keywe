package com.kiosk.order.application.port.outbound;

import com.kiosk.order.domain.OrderMenu;

public interface OrderMenuCommandPort {
    long create(OrderMenu orderMenu);
}
