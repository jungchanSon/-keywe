package com.kiosk.infra.rdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(catalog = "order", name = "order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class OrderEntity {

    @Id
    private long orderId;

    private long shopId;

    private long userId;

    private String phoneNumber;
}
