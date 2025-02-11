package com.kiosk.order.application.port.inbound;

import com.kiosk.order.application.service.order.vo.OrderVO;

public interface OrderCommand {
    long create(OrderVO.save save);
}
