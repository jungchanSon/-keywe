package com.kiosk.server.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
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

    private final SecretKey secretKey;
    private final long expirationTime;

    private static final String ROLE_CLAIM = "role";
    private static final String TOKEN_TYPE = "tokenType";

    @Autowired
    public TokenUtil(@Value("${jwt.secret-key}") String secretKey, @Value("${jwt.expiration-time}") long expirationTime) {
        this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), "HmacSHA256");
        this.expirationTime = expirationTime;
    }

    public String createTemporaryToken(long userId) {
        return generateTemporaryToken(userId, "CUSTOMER", "temp", 60000L);
    }

    public String createAccessToken(long userId, long profileId, String role) {
        return generateAccessToken(userId, profileId, role, "auth", expirationTime);
    }

    private String generateTemporaryToken(long userId, String role, String tokenType, long expirationTime) {
        ClaimsBuilder claimsBuilder = Jwts.claims();

        claimsBuilder.subject(String.valueOf(userId));
        claimsBuilder.add(TOKEN_TYPE, tokenType);
        claimsBuilder.add(ROLE_CLAIM, role);

        Claims claims = claimsBuilder.build();
        return generateToken(claims, new Date(System.currentTimeMillis() + expirationTime));
    }

    // generateAccessToken 추가
    private String generateAccessToken(long userId, long profileId, String role, String tokenType, long expirationTime) {
        ClaimsBuilder claimsBuilder = Jwts.claims();

        claimsBuilder.subject(String.valueOf(userId));
        claimsBuilder.add("profileId", String.valueOf(profileId));
        claimsBuilder.add(TOKEN_TYPE, tokenType);
        claimsBuilder.add(ROLE_CLAIM, role);

        Claims claims = claimsBuilder.build();
        return generateToken(claims, new Date(System.currentTimeMillis() + expirationTime));
    }

    private String generateToken(Claims claims, Date expiredTime) {
        return Jwts.builder().claims(claims).issuedAt(new Date(System.currentTimeMillis())).expiration(expiredTime).signWith(secretKey).compact();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

}
