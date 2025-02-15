package com.kiosk.server.order.application.service.order;

import com.kiosk.server.order.application.port.inbound.OrderMenuCommand;
import com.kiosk.server.order.application.port.outbound.OrderMenuCommandPort;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import com.kiosk.server.order.domain.mapper.OrderMenuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMenuWriter implements OrderMenuCommand {

    private final OrderMenuCommandPort orderMenuCommandPort;

    private final OrderMenuMapper orderMenuMapper;

    @Override
    @Transactional
    public long create(OrderVO.OrderMenu orderMenu, long orderId) {
        log.info("[TRANSACTION-SUB2-START]");
        long orderMenuId = orderMenuCommandPort.create(orderMenuMapper.toDomain(orderMenu, orderId));
        log.info("[TRANSACTION-SUB2-END]");

        return orderMenuId;
    }
}
