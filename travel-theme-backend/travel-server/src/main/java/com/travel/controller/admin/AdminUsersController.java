package com.travel.controller.admin;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.dto.UserStatusUpdateDTO;
import com.travel.pojo.entity.User;
import com.travel.service.AdminUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final AdminUserService adminUserService;

    public AdminUsersController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ApiResponse<List<User>> users() {
        return ApiResponse.ok(adminUserService.listUsers());
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody UserStatusUpdateDTO dto) {
        adminUserService.updateStatus(id, dto == null ? null : dto.getStatus());
        return ApiResponse.ok("更新成功", null);
    }
}

