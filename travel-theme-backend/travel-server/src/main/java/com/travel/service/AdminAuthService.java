package com.travel.service;

import com.travel.common.error.BusinessException;
import com.travel.common.error.ErrorCode;
import com.travel.common.jwt.JwtClaims;
import com.travel.common.jwt.JwtUtil;
import com.travel.mapper.AdminMapper;
import com.travel.pojo.dto.AuthLoginDTO;
import com.travel.pojo.entity.Admin;
import com.travel.pojo.vo.AuthTokenVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AdminAuthService {
    private final AdminMapper adminMapper;

    @Value("${sky.jwt.admin-secret-key}")
    private String adminSecretKey;

    @Value("${sky.jwt.admin-ttl}")
    private long adminTtl;

    public AdminAuthService(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    public AuthTokenVO login(AuthLoginDTO dto) {
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        Admin admin = adminMapper.findByUsername(dto.getUsername());
        if (admin == null || !PasswordUtil.sha256(dto.getPassword()).equals(admin.getPasswordHash())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "账号或密码错误");
        }
        String token = JwtUtil.generateToken(
                adminSecretKey,
                adminTtl,
                Map.of(
                        JwtClaims.USER_ID, admin.getId(),
                        JwtClaims.USERNAME, admin.getUsername(),
                        JwtClaims.ROLE, "ADMIN"
                )
        );
        return new AuthTokenVO(token);
    }
}

