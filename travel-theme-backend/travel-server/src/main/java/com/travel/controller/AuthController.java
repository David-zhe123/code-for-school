package com.travel.controller;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.dto.AuthLoginDTO;
import com.travel.pojo.dto.AuthRegisterDTO;
import com.travel.pojo.vo.AuthTokenVO;
import com.travel.pojo.vo.UserProfileVO;
import com.travel.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@RequestBody AuthRegisterDTO dto) {
        authService.register(dto);
        return ApiResponse.ok("注册成功", null);
    }

    @PostMapping("/login")
    public ApiResponse<AuthTokenVO> login(@RequestBody AuthLoginDTO dto) {
        return ApiResponse.ok("登录成功", authService.login(dto));
    }

    @GetMapping("/profile")
    public ApiResponse<UserProfileVO> profile() {
        return ApiResponse.ok(authService.profile());
    }
}

