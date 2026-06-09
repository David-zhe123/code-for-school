package com.travel.pojo.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {
    private Long id;
    private String username;
    private Integer totalPrice;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemVO> items;
}

