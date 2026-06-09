package com.travel.service;

import com.travel.common.error.BusinessException;
import com.travel.common.error.ErrorCode;
import com.travel.common.jwt.JwtClaims;
import com.travel.common.jwt.JwtUtil;
import com.travel.interceptor.UserContext;
import com.travel.mapper.UserMapper;
import com.travel.pojo.dto.AuthLoginDTO;
import com.travel.pojo.dto.AuthRegisterDTO;
import com.travel.pojo.entity.User;
import com.travel.pojo.vo.AuthTokenVO;
import com.travel.pojo.vo.UserProfileVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthService {
    private final UserMapper userMapper;

    @Value("${sky.jwt.user-secret-key}")
    private String userSecretKey;

    @Value("${sky.jwt.user-ttl}")
    private long userTtl;

    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void register(AuthRegisterDTO dto) {
        if (dto == null || dto.getUsername() == null || dto.getUsername().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        User exists = userMapper.findByUsername(dto.getUsername());
        if (exists != null) {
            throw new BusinessException(ErrorCode.USERNAME_EXISTS, "用户名已存在，请更换");
        }

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPasswordHash(PasswordUtil.sha256(dto.getPassword()));
        user.setStatus("NORMAL");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);
    }

    public AuthTokenVO login(AuthLoginDTO dto) {
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            throw new IllegalArgumentException("用户名和密码不能为空");
        }
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null || !PasswordUtil.sha256(dto.getPassword()).equals(user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED, "用户名或密码错误");
        }
        if (!"NORMAL".equalsIgnoreCase(user.getStatus())) {
            throw new BusinessException(ErrorCode.USER_DISABLED, "账号已被禁用，请联系管理员");
        }
        String token = JwtUtil.generateToken(
                userSecretKey,
                userTtl,
                Map.of(
                        JwtClaims.USER_ID, user.getId(),
                        JwtClaims.USERNAME, user.getUsername(),
                        JwtClaims.ROLE, "USER"
                )
        );
        return new AuthTokenVO(token);
    }

    public UserProfileVO profile() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未登录");
        }
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "用户不存在");
        }
        return new UserProfileVO(user.getId(), user.getUsername(), user.getStatus());
    }
}

