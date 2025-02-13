package com.kiosk.server.common.config;

import enums.TokenClaimName;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class WebSocketInterceptor implements ChannelInterceptor {

    private static final String TOPIC_PREFIX = "/topic/";

    private final SecretKey secretKey;

    public WebSocketInterceptor(@Value("${jwt.secret}") String SECRET_KEY) {
        this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        switch (accessor.getCommand()) {
            case CONNECT -> handleConnect(accessor);
            case SUBSCRIBE -> handleSubscribe(accessor);
        }

        return message;
    }

    public void handleConnect(StompHeaderAccessor accessor) {
        String bearerToken = accessor.getFirstNativeHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            log.debug("Authorization header is missing or invalid");
            throw new MessageDeliveryException("Invalid authorization header");
        }

        try {
            String token = bearerToken.substring(7);
            Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            String userId = claims.getSubject();
            String profileId = (String) claims.get(TokenClaimName.PROFILE_ID);
            accessor.getSessionAttributes().put("familyId", userId);
            accessor.getSessionAttributes().put("userId", profileId);

        } catch (JwtException e) {
            log.debug("JWT validation failed", e);
            throw new MessageDeliveryException("Invalid token");
        }
    }

    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = Objects.requireNonNull(accessor.getDestination());
        String userId = (String) getSessionAttribute(accessor, "userId");

        if (!destination.startsWith(TOPIC_PREFIX)) {
            throw new MessageDeliveryException("Invalid destination");
        }

        String targetUserID = destination.substring(TOPIC_PREFIX.length());
        if (!userId.equals(targetUserID)) {
            throw new MessageDeliveryException("Invalid user ID");
        }
    }

    private Object getSessionAttribute(StompHeaderAccessor accessor, String key) {
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        return sessionAttributes.getOrDefault(key, null);
    }

}
