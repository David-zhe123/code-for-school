package com.travel.controller.admin;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.dto.ScenicUpsertDTO;
import com.travel.pojo.entity.ScenicSpot;
import com.travel.service.ScenicService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/scenic")
public class AdminScenicController {
    private final ScenicService scenicService;

    public AdminScenicController(ScenicService scenicService) {
        this.scenicService = scenicService;
    }

    @PostMapping
    public ApiResponse<ScenicSpot> create(@RequestBody ScenicUpsertDTO dto) {
        return ApiResponse.ok("创建成功", scenicService.create(dto));
    }

    @PutMapping("/{id}")
    public ApiResponse<ScenicSpot> update(@PathVariable Long id, @RequestBody ScenicUpsertDTO dto) {
        return ApiResponse.ok("更新成功", scenicService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        scenicService.remove(id);
        return ApiResponse.ok("删除成功", null);
    }
}

