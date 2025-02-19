package com.kiosk.server.order.domain;

import com.kiosk.server.infra.rdb.entity.outbox.OutBoxStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class OutboxOrder {

    long receiveId;
    long orderId;
    long shopId;
    long customerId;
    long totalPrice;
    LocalDateTime createdAt;
    OutBoxStatus outBoxStatus;
}
