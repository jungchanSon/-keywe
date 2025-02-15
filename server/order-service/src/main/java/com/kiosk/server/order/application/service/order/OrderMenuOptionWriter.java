package com.kiosk.server.order.application.service.order;

import com.kiosk.server.order.application.port.inbound.OrderMenuOptionCommand;
import com.kiosk.server.order.application.port.outbound.OrderMenuOptionCommandPort;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import com.kiosk.server.order.domain.mapper.OrderMenuOptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderMenuOptionWriter implements OrderMenuOptionCommand {

    private final OrderMenuOptionCommandPort orderMenuOptionCommandPort;

    private final OrderMenuOptionMapper orderMenuOptionMapper;

    @Override
    @Transactional
    public long create(OrderVO.OrderMenuOption orderMenuOption, long orderId, long orderMenuId) {
        log.info("[TRANSACTION-SUB3-START]");
        long orderMenuOptionId = orderMenuOptionCommandPort.create(
                orderMenuOptionMapper.toDomain(orderMenuOption, orderId, orderMenuId)
        );
        log.info("[TRANSACTION-SUB3-END]");
        return orderMenuOptionId;
    }
}
