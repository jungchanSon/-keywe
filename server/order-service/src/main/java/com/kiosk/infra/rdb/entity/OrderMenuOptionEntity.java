package com.kiosk.infra.rdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "order", name = "order_menu_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class OrderMenuOptionEntity {

    @Id
    private long orderMenuOptionId;

    private long orderId;

    private long orderMenuId;

    private long optionValueId;

    private long optionCount;
}
