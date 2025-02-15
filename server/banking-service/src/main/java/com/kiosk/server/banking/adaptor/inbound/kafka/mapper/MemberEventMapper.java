package com.kiosk.server.banking.adaptor.inbound.kafka.mapper;

import com.kiosk.server.banking.application.service.banking.vo.AccountVO;
import com.kiosk.server.infra.kafka.event.MemberEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface MemberEventMapper {

    AccountVO.Save toAccountVO (MemberEvent.CreatedEvent memberEvent);
}
