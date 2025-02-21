package com.kiosk.server.infra.rdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "order", name = "order_menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class OrderMenuEntity {

    @Id
    private long orderMenuId;

    private long orderId;

    private long menuId;

    private long menuCount;
}
