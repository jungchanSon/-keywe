package com.kiosk.server.order.application.port.inbound;

import com.kiosk.server.order.application.service.order.vo.OrderVO;

public interface OrderMenuCommand {
    long create(OrderVO.OrderMenu orderMenu, long orderId);
}
