package com.travel.controller;

import com.travel.common.api.ApiResponse;
import com.travel.pojo.vo.OrderVO;
import com.travel.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private final OrderService orderService;

    public OrdersController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ApiResponse<OrderVO> create() {
        return ApiResponse.ok("下单成功", orderService.createFromItinerary());
    }

    @GetMapping("/my")
    public ApiResponse<List<OrderVO>> myList() {
        return ApiResponse.ok(orderService.myOrders());
    }
}

