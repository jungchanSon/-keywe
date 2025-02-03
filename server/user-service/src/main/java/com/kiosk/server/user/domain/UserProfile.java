package com.kiosk.server.user.domain;

import com.kiosk.server.common.util.IdUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserProfile {

    private long profileId;
    private long userId;
    private String profileName;
    private ProfileRole profileRole;
    private String phoneNumber;
    private String profilePass; // 부모일 경우에만 값이 설정됨
    private LocalDateTime regDate;

    private UserProfile(long profileId, long userId, String profileName, ProfileRole profileRole, String phoneNumber, String profilePass, LocalDateTime regDate) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileName = profileName;
        this.profileRole = profileRole;
        this.phoneNumber = phoneNumber;
        this.profilePass = profilePass;
        this.regDate = regDate;
    }

    public static UserProfile createChild(long userId, String profileName, ProfileRole profileRole, String phoneNumber) {
        return new UserProfile(
                IdUtil.create(),
                userId,
                profileName,
                profileRole,
                phoneNumber,
                null,
                LocalDateTime.now()
        );
    }

    public static UserProfile createParent(long userId, String profileName, ProfileRole profileRole, String phoneNumber, String profilePass) {
        return new UserProfile(
                IdUtil.create(),
                userId,
                profileName,
                profileRole,
                phoneNumber,
                profilePass,
                LocalDateTime.now()
        );
    }

}
