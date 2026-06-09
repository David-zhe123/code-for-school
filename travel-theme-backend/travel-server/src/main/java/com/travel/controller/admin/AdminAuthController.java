package com.travel.controller.admin;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.dto.AuthLoginDTO;
import com.travel.pojo.vo.AuthTokenVO;
import com.travel.service.AdminAuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminAuthController {
    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public ApiResponse<AuthTokenVO> login(@RequestBody AuthLoginDTO dto) {
        return ApiResponse.ok("登录成功", adminAuthService.login(dto));
    }
}

