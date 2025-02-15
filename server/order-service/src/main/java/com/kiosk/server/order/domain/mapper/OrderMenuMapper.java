package com.kiosk.server.order.domain.mapper;

import com.kiosk.server.infra.rdb.entity.OrderMenuEntity;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import com.kiosk.server.order.domain.OrderMenu;
import com.kiosk.server.common.util.IdUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMenuMapper {

    OrderMenu toDomain(OrderVO.OrderMenu orderMenu, long orderId);
    OrderMenuEntity toEntity(OrderMenu orderMenu);

    @AfterMapping
    default void setOrderMenuId(@MappingTarget OrderMenu orderMenu) {
        orderMenu.setOrderMenuId(IdUtil.create());
    }
}
