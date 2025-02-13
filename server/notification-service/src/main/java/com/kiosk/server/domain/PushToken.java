package com.kiosk.server.domain;

import com.kiosk.server.common.util.IdUtil;
import com.kiosk.server.common.exception.UnauthorizedProfileAccessException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "push_tokens")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushToken {

    @Id
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "profile_id", nullable = false)
    private Long profileId;

    @Column(name = "device_id", nullable = false, length = 100)
    private String deviceId;

    @Column(name = "token", nullable = false)
    private String token;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public PushToken(Long userId, Long profileId, String deviceId, String token) {
        this.id = IdUtil.create();
        this.userId = userId;
        this.profileId = profileId;
        this.deviceId = deviceId;
        this.token = token;
    }

    public void verifyProfileAccess(Long requestedProfileId) {
        if (!this.profileId.equals(requestedProfileId)) {
            throw new UnauthorizedProfileAccessException(requestedProfileId, this.profileId);
        }
    }
}
