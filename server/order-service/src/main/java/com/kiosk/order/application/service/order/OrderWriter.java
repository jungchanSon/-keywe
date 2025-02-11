package com.kiosk.order.application.service.order;

import com.kiosk.order.application.port.inbound.OrderCommand;
import com.kiosk.order.application.port.outbound.OrderCommandPort;
import com.kiosk.order.application.port.outbound.OrderMenuCommandPort;
import com.kiosk.order.application.port.outbound.OrderMenuOptionCommandPort;
import com.kiosk.order.application.service.order.vo.OrderVO;
import com.kiosk.order.domain.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderWriter implements OrderCommand {

    private final OrderCommandPort orderCommandPort;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public long create(OrderVO.save order) {
        return orderCommandPort.create(orderMapper.toDomain(order));
    }
}
