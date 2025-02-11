package com.kiosk.order.application.service.order;

import com.kiosk.order.application.port.inbound.OrderMenuOptionCommand;
import com.kiosk.order.application.port.outbound.OrderMenuOptionCommandPort;
import com.kiosk.order.application.service.order.vo.OrderVO;
import com.kiosk.order.domain.mapper.OrderMenuOptionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderMenuOptionWriter implements OrderMenuOptionCommand {

    private final OrderMenuOptionCommandPort orderMenuOptionCommandPort;

    private final OrderMenuOptionMapper orderMenuOptionMapper;

    @Override
    @Transactional
    public long create(OrderVO.OrderMenuOption orderMenuOption, long orderId, long orderMenuId) {
       return orderMenuOptionCommandPort.create(
               orderMenuOptionMapper.toDomain(orderMenuOption, orderId, orderMenuId)
       );
    }
}
