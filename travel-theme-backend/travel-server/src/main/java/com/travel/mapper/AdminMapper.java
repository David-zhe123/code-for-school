package com.travel.mapper;

import com.travel.pojo.entity.Admin;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    Admin findByUsername(@Param("username") String username);

    int insert(Admin admin);
}

