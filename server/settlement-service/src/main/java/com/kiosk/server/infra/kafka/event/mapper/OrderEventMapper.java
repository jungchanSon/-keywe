package com.kiosk.server.infra.kafka.event.mapper;

import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.infra.kafka.event.OrderEvent;
import com.kiosk.server.settlement.domain.Menu;
import com.kiosk.server.settlement.domain.MenuOption;
import com.kiosk.server.settlement.domain.Receive;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderEventMapper {

    Receive toDomain(OrderEvent.CreatedEvent event);

    @Mapping(target="orderMenuId", source="menuId")
    @Mapping(target="count", source = "menuCount")
    @Mapping(target="menuOptionList", source="optionList")
    Menu toDomain(OrderEvent.Menu menu);

    @Mapping(target="orderOptionId", source = "optionValueId")
    @Mapping(target="count", source = "optionCount")
    MenuOption toDomain(OrderEvent.Option option);

    @AfterMapping
    default void setReceiveId(@MappingTarget Receive receive) {
        receive.setReceiveId(IdUtil.create());
    }

    @AfterMapping
    default void setMenuId(@MappingTarget Menu menu) {
        menu.setMenuId(IdUtil.create());
    }

    @AfterMapping
    default void setMenuOptionId(@MappingTarget MenuOption menuOption) {
        menuOption.setMenuOptionId(IdUtil.create());
    }
}
