package com.kiosk.order.application.port.inbound;

import com.kiosk.order.application.service.order.vo.OrderVO;

public interface OrderUseCase {
    long commandOrder(OrderVO.save order);
}
