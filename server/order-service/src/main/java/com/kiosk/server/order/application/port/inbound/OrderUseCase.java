package com.kiosk.server.order.application.port.inbound;

import com.kiosk.server.order.application.service.order.vo.OrderVO;

public interface OrderUseCase {
    long commandOrder(OrderVO.save order);
}
