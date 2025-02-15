package com.kiosk.server.order.adaptor.outbound;

import com.kiosk.server.order.application.port.outbound.OrderCommandPort;
import com.kiosk.server.infra.rdb.repository.OrderRepository;
import com.kiosk.server.order.domain.Order;
import com.kiosk.server.order.domain.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCommandAdaptor implements OrderCommandPort {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    public long create(Order order) {
        long orderId = orderRepository.save(orderMapper.toEntity(order)).getOrderId();
        log.info("Create order => id={}", orderId);

        return  orderId;
    }


}
