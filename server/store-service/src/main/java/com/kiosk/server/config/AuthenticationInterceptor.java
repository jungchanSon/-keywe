package com.kiosk.server.config;

import com.kiosk.server.common.exception.custom.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!StringUtils.hasLength(request.getHeader("userId"))) {
            throw new UnauthorizedException("인증 실패");
        }
        return true;
    }

}
