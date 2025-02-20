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
import org.springframework.data.annotation.Version;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(catalog = "account", name = "account")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AccountEntity {

    @Id
    private Long accountId;

    private Long memberId;

    private String email;

    private Long balance;

    private Long withdrawSnapshotId = 0L;

    private Long depositSnapshotId = 0L;

    private String phoneNumber;

    @Version
    private Long version;

    @CreatedDate
    private LocalDateTime createdAt;

    public Boolean minus(long amount) {
        if(this.balance < amount) return false;
        this.balance -= amount;

        return true;
    }

    public void plus(long amount) {
        this.balance += amount;
    }
}
