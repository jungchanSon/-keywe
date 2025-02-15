package com.kiosk.server.order.adaptor.inbound.api.mapper;

import com.kiosk.server.order.adaptor.inbound.api.dto.OrderRequestDTO;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderDTOMapper {

    OrderVO.save toVO(OrderRequestDTO.Order order, long shopId);
}
