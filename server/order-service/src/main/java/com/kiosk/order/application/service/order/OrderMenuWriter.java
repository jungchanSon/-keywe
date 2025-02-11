package com.kiosk.order.application.service.order;

import com.kiosk.order.application.port.inbound.OrderMenuCommand;
import com.kiosk.order.application.port.outbound.OrderMenuCommandPort;
import com.kiosk.order.application.service.order.vo.OrderVO;
import com.kiosk.order.domain.mapper.OrderMenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderMenuWriter implements OrderMenuCommand {

    private final OrderMenuCommandPort orderMenuCommandPort;

    private final OrderMenuMapper orderMenuMapper;

    @Override
    @Transactional
    public long create(OrderVO.OrderMenu orderMenu, long orderId) {
        return orderMenuCommandPort.create(orderMenuMapper.toDomain(orderMenu, orderId));
    }
}
