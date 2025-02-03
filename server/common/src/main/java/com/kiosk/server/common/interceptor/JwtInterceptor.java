package com.kiosk.server.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String tokenType = (String) request.getAttribute("tokenType");

        if (tokenType == null || tokenType.isBlank()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("Missing tokenType");
            return false;
        }

        if ("TEMP".equals(tokenType)) {
            String requestURI = request.getRequestURI();
            String method = request.getMethod();


            if (("GET".equalsIgnoreCase(method) && "/user/profile/list".equals(requestURI)) ||
                    ("POST".equalsIgnoreCase(method) && "/user/profile".equals(requestURI))) {
                return true;
            }

            if ("GET".equalsIgnoreCase(method) && requestURI.matches("/user/profile/\\d+")) {
                return true;
            }

            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println("Forbidden URI");
            return false;
        }

        if ("AUTH".equals(tokenType)) {

            Long profileId = (Long) request.getAttribute("profileId");
            if (profileId == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("profileId is required for AUTH tokenType");
                return false;
            }
            return true;
        }

        response.sendError(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().println("Unknown tokenType");
        return false;
    }
}
