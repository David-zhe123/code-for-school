package com.travel.mapper;

import com.travel.pojo.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    User findById(@Param("id") Long id);

    User findByUsername(@Param("username") String username);

    int insert(User user);

    int updateStatus(@Param("id") Long id, @Param("status") String status);

    List<User> findAll();
}

