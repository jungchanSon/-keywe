package com.kiosk.server.order.domain.mapper;

import com.kiosk.server.infra.rdb.entity.OrderEntity;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import com.kiosk.server.order.domain.Order;
import com.kiosk.server.common.util.IdUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order toDomain(OrderVO.save save);
    OrderEntity toEntity(Order order);

    @AfterMapping
    default void setOrderId(@MappingTarget Order order) {
        order.setOrderId(IdUtil.create());
    }
}
