package com.kiosk.server.order.application.port.inbound;

import com.kiosk.server.order.application.service.order.vo.OrderVO;

public interface OrderCommand {
    long create(OrderVO.save save);
}
