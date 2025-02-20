package com.kiosk.server.infra.rdb.entity;

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
@Table(catalog = "settlement", name = "receive")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReceiveEntity {
    @Id
    private long receiveId;

    private long orderId;

    private long shopId;

    private long customerId;

    private long totalPrice;

    @Enumerated(EnumType.STRING)
    private OutBoxStatus outboxStatus;

    @CreatedDate
    private LocalDateTime createdAt;
}
