package com.kiosk.server.order.application.port.outbound;

import com.kiosk.server.order.domain.OutboxMenu;
import com.kiosk.server.order.domain.OutboxOption;
import com.kiosk.server.order.domain.OutboxOrder;

import java.util.List;

public interface OutboxQueryPort {

    OutboxOrder findById(Long id);
    List<OutboxOrder> searchOutboxOrderPending();
    List<OutboxMenu> searchOutboxMenuByOrderId(Long orderId);
    List<OutboxOption> searchOutboxOptionByOrderId(Long orderId);
}
