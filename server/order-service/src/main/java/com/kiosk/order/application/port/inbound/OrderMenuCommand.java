package com.kiosk.order.application.port.inbound;

import com.kiosk.order.application.service.order.vo.OrderVO;

public interface OrderMenuCommand {
    long create(OrderVO.OrderMenu orderMenu, long orderId);
}
