package com.travel.controller.admin;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.dto.OrderStatusUpdateDTO;
import com.travel.pojo.vo.OrderVO;
import com.travel.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/orders")
public class AdminOrdersController {
    private final OrderService orderService;

    public AdminOrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResponse<List<OrderVO>> list() {
        return ApiResponse.ok(orderService.adminList());
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestBody OrderStatusUpdateDTO dto) {
        orderService.adminUpdateStatus(id, dto == null ? null : dto.getStatus());
        return ApiResponse.ok("更新成功", null);
    }
}

