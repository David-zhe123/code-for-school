package com.travel.controller;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.entity.ScenicSpot;
import com.travel.service.ScenicService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scenic")
public class ScenicController {
    private final ScenicService scenicService;

    public ScenicController(ScenicService scenicService) {
        this.scenicService = scenicService;
    }

    @GetMapping
    public ApiResponse<List<ScenicSpot>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category
    ) {
        return ApiResponse.ok(scenicService.list(keyword, category));
    }

    @GetMapping("/{id}")
    public ApiResponse<ScenicSpot> detail(@PathVariable Long id) {
        return ApiResponse.ok(scenicService.detail(id));
    }
}

