package com.kiosk.server.settlement.domain;

import com.kiosk.server.infra.rdb.entity.OutBoxStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Receive {
    long receiveId;
    long orderId;
    long shopId;
    long customerId;
    long totalPrice;
    OutBoxStatus outboxStatus;
    List<Menu> menuList;
    LocalDateTime createdAt;
}
