package com.kiosk.server.order.adaptor.outbound;

import com.kiosk.server.infra.rdb.repository.OrderMenuRepository;
import com.kiosk.server.order.application.port.outbound.OrderMenuCommandPort;
import com.kiosk.server.order.domain.OrderMenu;
import com.kiosk.server.order.domain.mapper.OrderMenuMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMenuCommandAdaptor implements OrderMenuCommandPort {

    private final OrderMenuRepository orderMenuRepository;

    private final OrderMenuMapper orderMenuMapper;

    @Override
    public long create(OrderMenu orderMenu) {
        long orderMenuId = orderMenuRepository.save(orderMenuMapper.toEntity(orderMenu)).getOrderMenuId();
        log.info("Create order menu. id={}", orderMenuId);

        return orderMenuId;
    }
}
