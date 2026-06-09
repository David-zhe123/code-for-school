package com.travel.interceptor;

import com.travel.common.error.BusinessException;
import com.travel.common.error.ErrorCode;
import com.travel.common.jwt.JwtClaims;
import com.travel.common.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Value("${sky.jwt.user-secret-key}")
    private String userSecretKey;

    @Value("${sky.jwt.admin-secret-key}")
    private String adminSecretKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/scenic")
                || path.startsWith("/api/admin/login")
                || "OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = resolveToken(request);
        if (token == null || token.isBlank()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未登录");
        }

        Claims claims;
        try {
            if (path.startsWith("/api/admin")) {
                claims = JwtUtil.parseToken(adminSecretKey, token);
            } else {
                claims = JwtUtil.parseToken(userSecretKey, token);
            }
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "登录已过期，请重新登录");
        }

        Object userIdObj = claims.get(JwtClaims.USER_ID);
        Long userId = userIdObj == null ? null : Long.valueOf(String.valueOf(userIdObj));
        String role = String.valueOf(claims.get(JwtClaims.ROLE));
        UserContext.set(userId, role);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private String resolveToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring("Bearer ".length());
        }
        String token = request.getHeader("token");
        if (token != null && !token.isBlank()) {
            return token;
        }
        return null;
    }
}

