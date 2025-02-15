package com.kiosk.banking.adaptor.inbound.kafka.mapper;

import com.kiosk.banking.application.service.banking.vo.AccountVO;
import com.kiosk.infra.kafka.event.MemberEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface MemberEventMapper {

    AccountVO.Save toAccountVO (MemberEvent.CreatedEvent memberEvent);
}
