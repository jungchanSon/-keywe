package com.kiosk.server.config;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import enums.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String userId = request.getHeader("userId");
        validateIdFormat(userId, "Invalid or missing userId");

        String tokenType = request.getHeader("tokenType");
        if (tokenType == null || (!tokenType.equals(TokenType.TEMP) && !tokenType.equals(TokenType.AUTH))) {
            log.error("Invalid JWT token: Invalid token type");
            throw new UnauthorizedException("JWT Authentication Failed");
        }

        if (TokenType.AUTH.equals(tokenType)) {
            String profileId = request.getHeader("profileId");
            validateIdFormat(profileId, "Invalid or missing profileId");
        }
        return true;
    }

    private void validateIdFormat(String idValue, String errorMessage) {
        try {
            Long.parseLong(idValue);
        } catch (NumberFormatException e) {
            log.error(errorMessage);
            throw new UnauthorizedException("토큰 인증 실패");
        }
    }
}
