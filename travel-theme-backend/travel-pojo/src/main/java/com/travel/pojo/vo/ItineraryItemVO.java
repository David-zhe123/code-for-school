package com.travel.pojo.vo;

import lombok.Data;

@Data
public class ItineraryItemVO {
    private Long id;
    private Long scenicId;
    private String name;
    private Integer price;
    private String note;
    private Integer sortOrder;
}

