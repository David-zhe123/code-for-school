package com.travel.mapper;

import com.travel.pojo.entity.Order;
import com.travel.pojo.entity.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {
    int insertOrder(Order order);

    int insertOrderItems(@Param("items") List<OrderItem> items);

    List<Order> listByUserId(@Param("userId") Long userId);

    List<Order> listAll();

    List<OrderItem> listItems(@Param("orderId") Long orderId);

    int updateStatus(@Param("id") Long id, @Param("status") String status);
}

