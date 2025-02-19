package com.kiosk.server.infra.rdb.entity.outbox;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(catalog = "settlement", name = "menu_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class OutboxMenuOptionEntity {

    @Id
    private long menuOptionId;

    private long orderOptionId;

    private long menuId;

    private String name;

    private Long price;

    private Long count;
}
