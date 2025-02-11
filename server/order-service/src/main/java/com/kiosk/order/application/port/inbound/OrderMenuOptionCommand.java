package com.kiosk.order.application.port.inbound;

import com.kiosk.order.application.service.order.vo.OrderVO;

public interface OrderMenuOptionCommand {
    long create(OrderVO.OrderMenuOption orderMenuOption, long orderId, long orderMenuId);
}
