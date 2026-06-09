package com.travel.service;

import com.travel.mapper.UserMapper;
import com.travel.pojo.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {
    private final UserMapper userMapper;

    public AdminUserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> listUsers() {
        return userMapper.findAll();
    }

    public void updateStatus(Long id, String status) {
        userMapper.updateStatus(id, status);
    }
}

