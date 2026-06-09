package com.travel.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Order {
    private Long id;
    private Long userId;
    private Integer totalPrice;
    /**
     * PENDING_TRIP / CONFIRMED / CANCELLED / FINISHED
     * 也可以直接存中文：待出行/已确认/已取消/已完成（按你前端显示习惯）
     */
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

