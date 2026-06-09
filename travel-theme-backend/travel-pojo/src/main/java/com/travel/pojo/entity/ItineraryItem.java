package com.travel.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItineraryItem {
    private Long id;
    private Long userId;
    private Long scenicId;
    private String note;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

