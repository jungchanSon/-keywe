package com.kiosk.server.infra.rdb.entity.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@Table(catalog = "outbox_order", name = "order_menu_option")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OutboxOrderMenuOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderMenuOptionId;

    private long orderId;

    private long optionValueId;

    private long optionCount;
}
