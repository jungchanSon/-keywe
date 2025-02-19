package com.kiosk.server.infra.rdb.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(catalog = "balance_history ", name = "deposit")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DepositEventEntity {

    @Id
    private Long depositEventId;

    private Long userId;

    private Long amount;

    private Long remainingBalance;

    @CreatedDate
    private LocalDateTime localDateTime;
}
