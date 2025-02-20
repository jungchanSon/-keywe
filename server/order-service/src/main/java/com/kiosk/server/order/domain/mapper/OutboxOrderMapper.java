package com.kiosk.server.order.domain.mapper;

import com.kiosk.server.infra.kafka.event.OrderEvent;
import com.kiosk.server.infra.rdb.entity.outbox.OutBoxStatus;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderEntity;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuEntity;
import com.kiosk.server.infra.rdb.entity.outbox.OutboxOrderMenuOptionEntity;
import com.kiosk.server.order.domain.OutboxMenu;
import com.kiosk.server.order.domain.OutboxOption;
import com.kiosk.server.order.domain.OutboxOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OutboxOrderMapper {

    OutboxOrderEntity toOutboxOrderEntity(OrderEvent.OrderCreatedEvent orderEvent, OutBoxStatus  outBoxStatus);

    OutboxOrderMenuEntity toOutboxOrderMenuEntity(OrderEvent.Menu menuEvent);

    OutboxOrderMenuOptionEntity toOutboxOrderMenuOptionEntity(OrderEvent.Option optionEvent);

    OutboxOrder toOutboxOrderDomain(OutboxOrderEntity outboxOrderEntity);

    OutboxMenu toOutboxMenuDomain(OutboxOrderMenuEntity entity);

    OutboxOption toOutboxOptionDomain(OutboxOrderMenuOptionEntity entity);

    OutboxOrderEntity toEntityFromDomain(OutboxOrder order);
}
