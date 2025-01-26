package com.kiosk.server.common.filter;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import com.kiosk.server.common.util.TokenUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter implements Filter {

    private final TokenUtil tokenUtil;
    private List<String> excludedPaths = List.of("/auth/user/login", "/user");

    public JwtFilter(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();

        // 필터에서 제외할 경로 처리
        if (isExcludedPath(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = validateJwtToken(httpRequest);
            setRequestAttributes(httpRequest, claims);

            filterChain.doFilter(request, response);
        } catch (UnauthorizedException e) {
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    // 제외 경로 확인
    private boolean isExcludedPath(String requestURI) {
        for (String excludeUrl : excludedPaths) {
            if (requestURI.equals(excludeUrl)) {
                return true;
            }
        }
        return false;
    }

    // JWT 토큰을 검증하고 Claims 반환
    private Claims validateJwtToken(HttpServletRequest httpRequest) {
        String token = isBearerToken(httpRequest);

        try {
            return tokenUtil.extractClaims(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid JWT token");
        }
    }

    // Authorization 헤더에서 Bearer 토큰 추출 및 검증
    private String isBearerToken(HttpServletRequest httpRequest) {
        String bearerToken = httpRequest.getHeader("Authorization");

        if (bearerToken == null) {
            throw new UnauthorizedException("Authorization header is missing");
        }

        if (!bearerToken.startsWith("Bearer ")) {
            throw new UnauthorizedException("Invalid Authorization header format");
        }

        return bearerToken.substring(7);
    }

    // 요청 속성에 사용자 정보 및 토큰 정보를 설정
    private void setRequestAttributes(HttpServletRequest httpRequest, Claims claims) {
        long userId = getValidatedLongClaim(claims, "userId", "Invalid or missing userId");
        httpRequest.setAttribute("userId", userId);

        String tokenType = (String) claims.get("TYPE");
        validateTokenType(tokenType);
        httpRequest.setAttribute("tokenType", tokenType);

        if ("AUTH".equals(tokenType)) {
            long profileId = getValidatedLongClaim(claims, "profileId", "Invalid or missing profileId");
            httpRequest.setAttribute("profileId", profileId);
        }
    }

    // Claims에서 ID 값을 검증하고 반환
    private Long getValidatedLongClaim(Claims claims, String idValue, String errorMessage) {
        Object idObj = null;

        if (idValue.equals("profileId")) {
            idObj = claims.get(idValue);
        } else if (idValue.equals("userId")) {
            idObj = claims.getSubject();
        }

        if (idObj == null) {
            throw new UnauthorizedException(errorMessage);
        }

        try {
            return Long.parseLong(idObj.toString());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException(errorMessage);
        }
    }

    private void validateTokenType(String tokenType) {

        if (tokenType == null || (!tokenType.equals("TEMP") && !tokenType.equals("AUTH"))) {
            throw new UnauthorizedException("Invalid JWT token: Invalid token type");
        }
    }
}
