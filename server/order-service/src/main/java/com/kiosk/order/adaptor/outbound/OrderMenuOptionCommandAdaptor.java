package com.kiosk.order.adaptor.outbound;

import com.kiosk.infra.rdb.repository.OrderMenuOptionRepository;
import com.kiosk.order.application.port.outbound.OrderMenuOptionCommandPort;
import com.kiosk.order.domain.OrderMenuOption;
import com.kiosk.order.domain.mapper.OrderMenuOptionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMenuOptionCommandAdaptor implements OrderMenuOptionCommandPort {

    private final OrderMenuOptionRepository orderMenuOptionRepository;

    private final OrderMenuOptionMapper orderMenuOptionMapper;

    @Override
    public long create(OrderMenuOption orderMenuOption) {
        long orderMenuOptionId = orderMenuOptionRepository.save(orderMenuOptionMapper.toEntity(orderMenuOption))
                .getOrderMenuOptionId();
        log.info("create order menu option. id={} ", orderMenuOptionId);

        return orderMenuOptionId;
    }
}
