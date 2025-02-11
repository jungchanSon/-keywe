package com.kiosk.order.domain.mapper;

import com.kiosk.infra.rdb.entity.OrderEntity;
import com.kiosk.order.application.service.order.vo.OrderVO;
import com.kiosk.order.domain.Order;
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
