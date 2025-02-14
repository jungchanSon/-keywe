package com.kiosk.order.application.service.order;

import com.kiosk.order.application.port.inbound.OrderCommand;
import com.kiosk.order.application.port.outbound.OrderCommandPort;
import com.kiosk.order.application.port.outbound.OrderMenuCommandPort;
import com.kiosk.order.application.port.outbound.OrderMenuOptionCommandPort;
import com.kiosk.order.application.service.order.vo.OrderVO;
import com.kiosk.order.domain.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderWriter implements OrderCommand {

    private final OrderCommandPort orderCommandPort;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public long create(OrderVO.save order) {
        log.info("[TRANSACTION-SUB1-START]");
        long orderId = orderCommandPort.create(orderMapper.toDomain(order));
        log.info("[TRANSACTION-SUB1-END]");
        return orderId;
    }
}
