package com.travel.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String passwordHash;
    /**
     * NORMAL / DISABLED
     */
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

