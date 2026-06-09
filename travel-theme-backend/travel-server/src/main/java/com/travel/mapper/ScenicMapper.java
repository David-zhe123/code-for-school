package com.travel.mapper;

import com.travel.pojo.entity.ScenicSpot;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScenicMapper {
    ScenicSpot findById(@Param("id") Long id);

    List<ScenicSpot> search(@Param("keyword") String keyword, @Param("category") String category);

    int insert(ScenicSpot spot);

    int update(ScenicSpot spot);

    int delete(@Param("id") Long id);
}

