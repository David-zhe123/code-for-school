package com.travel.pojo.dto;

import lombok.Data;

@Data
public class ScenicUpsertDTO {
    private String name;
    private String category;
    private Integer price;
    private String address;
    private String image;
    private String description;
}

