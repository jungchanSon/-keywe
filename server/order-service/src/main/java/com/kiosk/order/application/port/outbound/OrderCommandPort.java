package com.kiosk.order.application.port.outbound;

import com.kiosk.order.domain.Order;

public interface OrderCommandPort {
    long create(Order order);
}
