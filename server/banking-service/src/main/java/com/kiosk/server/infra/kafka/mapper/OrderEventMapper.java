package com.kiosk.server.infra.kafka.mapper;

import com.kiosk.server.banking.application.service.banking.vo.DepositVO;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderEventMapper {

}
