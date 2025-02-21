package com.kiosk.server.order.application.port.inbound;

import com.kiosk.server.order.application.service.order.vo.OrderVO;

public interface OrderMenuOptionCommand {
    long create(OrderVO.OrderMenuOption orderMenuOption, long orderId, long orderMenuId);
}
