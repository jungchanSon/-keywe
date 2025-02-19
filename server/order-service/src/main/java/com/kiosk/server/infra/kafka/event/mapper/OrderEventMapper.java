package com.kiosk.server.infra.kafka.event.mapper;

import com.kiosk.server.infra.kafka.event.OrderCompensatingEvent;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderEventMapper {

    OrderEvent.OrderCreatedEvent toOrderCreatedEvent(OrderVO.save order, long orderId, Long customerId);
    OrderEvent.Menu toOrderEventMenu(OrderVO.OrderMenu menu);
    OrderEvent.Option toOrderEventOption(OrderVO.OrderMenuOption option);

    OrderCompensatingEvent.CompensatingEvent toOrderCompensatingEvent(Long userId, Long amount);


}
