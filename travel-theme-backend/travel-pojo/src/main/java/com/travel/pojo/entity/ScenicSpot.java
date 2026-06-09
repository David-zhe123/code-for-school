package com.travel.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScenicSpot {
    private Long id;
    private String name;
    private String category;
    private Integer price;
    private String address;
    private String image;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

