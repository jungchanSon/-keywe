package com.kiosk.server.infra.rdb.entity.outbox;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(catalog = "outbox_order", name = "order")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OutboxOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long outboxOrderId;

    private long orderId;

    private long shopId;

    private long customerId;

    private long totalPrice;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OutBoxStatus outBoxStatus;
}
