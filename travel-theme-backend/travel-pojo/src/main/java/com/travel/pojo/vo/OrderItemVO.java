package com.travel.pojo.vo;

import lombok.Data;

@Data
public class OrderItemVO {
    private Long scenicId;
    private String scenicName;
    private Integer scenicPrice;
    private String note;
    private Integer sortOrder;
}

