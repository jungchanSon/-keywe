package com.kiosk.server.config;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import enums.TokenType;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtFilter implements Filter {

    private final Set<String> excludedPaths = Set.of("/auth/user/login", "/user", "/user/swagger-ui/**", "/user/v3/api-docs");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // 필터에서 제외할 경로 처리
        if (excludedPaths.contains(requestURI) || requestURI.startsWith("/internal")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            validateRequestHeaders(httpRequest);

            filterChain.doFilter(request, response);

        } catch (UnauthorizedException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private void validateRequestHeaders(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        validateIdFormat(userId, "Invalid or missing userId");

        String tokenType = request.getHeader("tokenType");
        if (tokenType == null || (!tokenType.equals(TokenType.TEMP) && !tokenType.equals(TokenType.AUTH))) {
            throw new UnauthorizedException("Invalid JWT token: Invalid token type");
        }

        if (TokenType.AUTH.equals(tokenType)) {
            String profileId = request.getHeader("profileId");
            validateIdFormat(profileId, "Invalid or missing profileId");
        }
    }

    private void validateIdFormat(String idValue, String errorMessage) {
        try {
            Long.parseLong(idValue);
        } catch (NumberFormatException e) {
            throw new UnauthorizedException(errorMessage);
        }
    }
}
