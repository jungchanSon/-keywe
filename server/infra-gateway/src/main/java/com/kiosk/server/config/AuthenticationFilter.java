package com.kiosk.server.config;

import enums.TokenClaimName;
import enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.List;

@Order(0)
@Component
public class AuthenticationFilter implements GlobalFilter {

    private static final List<String> FORBIDDEN_HEADERS = List.of("userId", "profileId", "tokenType");

    private final SecretKey secretKey;

    public AuthenticationFilter(@Value("${jwt.secret}") String SECRET_KEY) {
        this.secretKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET_KEY), "HmacSHA256");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 금지된 헤더가 있는지 확인
        boolean hasForbiddenHeader = FORBIDDEN_HEADERS.stream()
            .anyMatch(header -> request.getHeaders().containsKey(header));

        if (hasForbiddenHeader) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.BAD_REQUEST);
            return response.setComplete();
        }

        ServerHttpRequest.Builder mutatedRequest = request.mutate();

        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

                String userId = claims.getSubject();
                String tokenType = claims.get(TokenClaimName.TOKEN_TYPE, String.class);

                mutatedRequest.header("userId", userId);
                mutatedRequest.header("tokenType", tokenType);

                if (TokenType.AUTH.equals(tokenType)) {
                    mutatedRequest.header("profileId", claims.get("profileId", String.class));
                }

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange.mutate()
            .request(mutatedRequest.build())
            .build());
    }
}
