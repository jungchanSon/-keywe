package com.kiosk.server.order.domain.mapper;

import com.kiosk.server.infra.rdb.entity.OrderMenuOptionEntity;
import com.kiosk.server.order.application.service.order.vo.OrderVO;
import com.kiosk.server.order.domain.OrderMenuOption;
import com.kiosk.server.common.util.IdUtil;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMenuOptionMapper {

    OrderMenuOption toDomain(OrderVO.OrderMenuOption option, long orderId, long orderMenuId);
    OrderMenuOptionEntity toEntity(OrderMenuOption option);

    @AfterMapping
    default void setOrderMenuOptionId(@MappingTarget OrderMenuOption option) {
        option.setOrderMenuOptionId(IdUtil.create());
    }}
