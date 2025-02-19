package com.kiosk.server.settlement.domain.mapper;

import com.kiosk.server.infra.rdb.entity.MenuEntity;
import com.kiosk.server.infra.rdb.entity.MenuOptionEntity;
import com.kiosk.server.infra.rdb.entity.ReceiveEntity;
import com.kiosk.server.settlement.domain.Menu;
import com.kiosk.server.settlement.domain.MenuOption;
import com.kiosk.server.settlement.domain.Receive;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiveMapper {

    ReceiveEntity toEntity(Receive receive);
    MenuEntity toEntity(Menu menu);
    MenuOptionEntity toEntity(MenuOption menuOption);

    Receive toDomain(ReceiveEntity receive);

}
