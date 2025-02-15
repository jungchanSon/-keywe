package com.kiosk.server.order.application.port.outbound;

import com.kiosk.server.order.domain.Order;

public interface OrderCommandPort {
    long create(Order order);
}
