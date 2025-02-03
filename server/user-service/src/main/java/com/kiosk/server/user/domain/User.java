package com.kiosk.server.user.domain;

import com.kiosk.server.common.util.HashUtil;
import com.kiosk.server.common.util.IdUtil;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User {

    private Long userId;
    private UserRole role;
    private String email;
    private String password;
    private String salt;
    private LocalDateTime regDate;

    public static User create(String email, String password) {
        User user = new User();
        user.userId = IdUtil.create();
        user.role = UserRole.CUSTOMER;
        user.email = email;
        user.salt = HashUtil.generateSalt();
        user.password = HashUtil.hashPassword(password, user.salt);
        user.regDate = LocalDateTime.now();
        return user;
    }

}
