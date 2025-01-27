package com.kiosk.server.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenUtil {

    private static final String TOKEN_TYPE_CLAIM = "TYPE";
    private static final String TOKEN_TYPE_TEMP = "TEMP";
    private static final String TOKEN_TYPE_AUTH = "AUTH";
    private static final String BEARER_PREFIX = "Bearer ";

    private final SecretKey SECRET_KEY;
    private final long TEMP_TOKEN_EXPIRATION_TIME_IN_MILLIS;

    @Autowired
    public TokenUtil(
        @Value("${JWT.SECRET_KEY}") String SECRET_KEY,
        @Value("${JWT.EXPIRATION_TIME.TEMP}") long TEMP_TOKEN_EXPIRATION_TIME_IN_MILLIS
    ) {
        this.SECRET_KEY = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");
        this.TEMP_TOKEN_EXPIRATION_TIME_IN_MILLIS = TEMP_TOKEN_EXPIRATION_TIME_IN_MILLIS;
    }

    public String createTemporaryToken(long userId) {
        Claims claims = Jwts.claims()
            .subject(String.valueOf(userId))
            .add(TOKEN_TYPE_CLAIM, TOKEN_TYPE_TEMP)
            .build();

        Date expiredAt = new Date(System.currentTimeMillis() + TEMP_TOKEN_EXPIRATION_TIME_IN_MILLIS);

        return generateToken(claims, expiredAt);
    }

    public String createAuthenticationToken(long userId, long profileId) {
        Claims claims = Jwts.claims()
                .subject(String.valueOf(userId))
                .add("profileId", String.valueOf(profileId))
                .add(TOKEN_TYPE_CLAIM, TOKEN_TYPE_AUTH)
                .build();

        Date expiredAt = new Date(System.currentTimeMillis() + TEMP_TOKEN_EXPIRATION_TIME_IN_MILLIS);

        return generateToken(claims, expiredAt);
    }

    private String generateToken(Claims claims, Date expiredTime) {
        return BEARER_PREFIX + Jwts.builder()
            .claims(claims)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(expiredTime)
            .signWith(SECRET_KEY)
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(SECRET_KEY)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

}
