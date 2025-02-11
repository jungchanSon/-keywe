package com.kiosk.order.domain.mapper;

import com.kiosk.infra.rdb.entity.OrderMenuEntity;
import com.kiosk.order.application.service.order.vo.OrderVO;
import com.kiosk.order.domain.OrderMenu;
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
