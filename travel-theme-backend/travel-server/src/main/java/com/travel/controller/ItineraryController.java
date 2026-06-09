package com.travel.controller;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.dto.ItineraryAddDTO;
import com.travel.pojo.dto.ItineraryUpdateDTO;
import com.travel.pojo.vo.ItineraryItemVO;
import com.travel.service.ItineraryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itinerary")
public class ItineraryController {
    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @GetMapping
    public ApiResponse<List<ItineraryItemVO>> list() {
        return ApiResponse.ok(itineraryService.listMine());
    }

    @PostMapping("/items")
    public ApiResponse<ItineraryItemVO> add(@RequestBody ItineraryAddDTO dto) {
        return ApiResponse.ok("已加入行程", itineraryService.add(dto));
    }

    @PutMapping("/items/{id}")
    public ApiResponse<Void> update(@PathVariable Long id, @RequestBody ItineraryUpdateDTO dto) {
        itineraryService.update(id, dto);
        return ApiResponse.ok("更新成功", null);
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        itineraryService.remove(id);
        return ApiResponse.ok("删除成功", null);
    }

    @PostMapping("/clear")
    public ApiResponse<Void> clear() {
        itineraryService.clear();
        return ApiResponse.ok("已清空", null);
    }
}

