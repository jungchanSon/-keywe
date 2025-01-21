package com.kiosk.server.common.util;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class HashUtil {

    /**
     * 비밀번호를 SHA-256 해시로 변환하는 메서드
     */

    public static String hashPassword(String password, String salt) {

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            messageDigest.update(salt.getBytes());

            byte[] hash = messageDigest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException("SHA-256 NOT FOUND", e);
        }
    }


    /**
     * 솔트 생성 메서드
     */
    public static String generateSalt() {

        byte[] salt = new byte[16];

        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);

        return Base64.getEncoder().encodeToString(salt);
    }
}
