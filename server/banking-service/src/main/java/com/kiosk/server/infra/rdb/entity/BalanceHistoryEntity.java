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
@Table(catalog = "balance_history ", name = "balance_history")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BalanceHistoryEntity {

    @Id
    private Long balanceHistoryId;

    private Long fromUserId;

    private Long toUserId;

    private Long amount;

    @CreatedDate
    private LocalDateTime createAt;
}
