package com.travel.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItem {
    private Long id;
    private Long orderId;
    private Long scenicId;
    private String scenicName;
    private Integer scenicPrice;
    private String note;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

