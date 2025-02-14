package com.kiosk.order.adaptor.outbound;

import com.kiosk.infra.rdb.repository.OrderMenuRepository;
import com.kiosk.order.application.port.outbound.OrderMenuCommandPort;
import com.kiosk.order.domain.OrderMenu;
import com.kiosk.order.domain.mapper.OrderMenuMapper;
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
