package com.travel.service;

import com.travel.common.error.BusinessException;
import com.travel.common.error.ErrorCode;
import com.travel.interceptor.UserContext;
import com.travel.mapper.ItineraryMapper;
import com.travel.mapper.OrderMapper;
import com.travel.mapper.ScenicMapper;
import com.travel.mapper.UserMapper;
import com.travel.pojo.entity.ItineraryItem;
import com.travel.pojo.entity.Order;
import com.travel.pojo.entity.OrderItem;
import com.travel.pojo.entity.ScenicSpot;
import com.travel.pojo.entity.User;
import com.travel.pojo.vo.OrderItemVO;
import com.travel.pojo.vo.OrderVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderMapper orderMapper;
    private final ItineraryMapper itineraryMapper;
    private final ScenicMapper scenicMapper;
    private final UserMapper userMapper;

    public OrderService(OrderMapper orderMapper, ItineraryMapper itineraryMapper, ScenicMapper scenicMapper, UserMapper userMapper) {
        this.orderMapper = orderMapper;
        this.itineraryMapper = itineraryMapper;
        this.scenicMapper = scenicMapper;
        this.userMapper = userMapper;
    }

    @Transactional
    public OrderVO createFromItinerary() {
        Long userId = requireUserId();
        List<ItineraryItem> itinerary = itineraryMapper.listByUserId(userId);
        if (itinerary.isEmpty()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "行程为空，无法下单");
        }

        int total = 0;
        List<OrderItem> orderItems = new ArrayList<>();
        int sort = 1;
        for (ItineraryItem item : itinerary) {
            ScenicSpot spot = scenicMapper.findById(item.getScenicId());
            if (spot == null) continue;
            total += spot.getPrice() == null ? 0 : spot.getPrice();

            OrderItem oi = new OrderItem();
            oi.setScenicId(spot.getId());
            oi.setScenicName(spot.getName());
            oi.setScenicPrice(spot.getPrice());
            oi.setNote(item.getNote());
            oi.setSortOrder(sort++);
            oi.setCreatedAt(LocalDateTime.now());
            oi.setUpdatedAt(LocalDateTime.now());
            orderItems.add(oi);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus("待出行");
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insertOrder(order);

        for (OrderItem oi : orderItems) {
            oi.setOrderId(order.getId());
        }
        if (!orderItems.isEmpty()) {
            orderMapper.insertOrderItems(orderItems);
        }

        itineraryMapper.clear(userId);
        return buildOrderVO(order, orderItems, userId);
    }

    public List<OrderVO> myOrders() {
        Long userId = requireUserId();
        List<Order> orders = orderMapper.listByUserId(userId);
        User user = userMapper.findById(userId);
        String username = user == null ? null : user.getUsername();

        List<OrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            List<OrderItem> items = orderMapper.listItems(order.getId());
            result.add(buildOrderVO(order, items, userId, username));
        }
        return result;
    }

    public List<OrderVO> adminList() {
        List<Order> orders = orderMapper.listAll();
        List<OrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            User user = userMapper.findById(order.getUserId());
            List<OrderItem> items = orderMapper.listItems(order.getId());
            result.add(buildOrderVO(order, items, order.getUserId(), user == null ? null : user.getUsername()));
        }
        return result;
    }

    public void adminUpdateStatus(Long id, String status) {
        if (status == null || status.isBlank()) throw new IllegalArgumentException("status 不能为空");
        orderMapper.updateStatus(id, status);
    }

    private Long requireUserId() {
        Long userId = UserContext.getUserId();
        if (userId == null) throw new BusinessException(ErrorCode.UNAUTHORIZED, "未登录");
        return userId;
    }

    private OrderVO buildOrderVO(Order order, List<OrderItem> items, Long userId) {
        User user = userMapper.findById(userId);
        return buildOrderVO(order, items, userId, user == null ? null : user.getUsername());
    }

    private OrderVO buildOrderVO(Order order, List<OrderItem> items, Long userId, String username) {
        OrderVO vo = new OrderVO();
        vo.setId(order.getId());
        vo.setUsername(username);
        vo.setTotalPrice(order.getTotalPrice());
        vo.setStatus(order.getStatus());
        vo.setCreatedAt(order.getCreatedAt());

        List<OrderItemVO> itemVOs = new ArrayList<>();
        for (OrderItem item : items) {
            OrderItemVO i = new OrderItemVO();
            i.setScenicId(item.getScenicId());
            i.setScenicName(item.getScenicName());
            i.setScenicPrice(item.getScenicPrice());
            i.setNote(item.getNote());
            i.setSortOrder(item.getSortOrder());
            itemVOs.add(i);
        }
        vo.setItems(itemVOs);
        return vo;
    }
}

