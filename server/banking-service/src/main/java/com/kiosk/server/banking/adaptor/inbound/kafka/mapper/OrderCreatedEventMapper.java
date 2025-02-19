package com.kiosk.server.banking.adaptor.inbound.kafka.mapper;

import com.kiosk.server.banking.application.service.banking.vo.DepositVO;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface OrderCreatedEventMapper {

    @Mapping(target = "userId", source="shopId")
    @Mapping(target = "amount", source="totalPrice")
    DepositVO.Save toVO(OrderEvent.OrderCreatedEvent event);
}
