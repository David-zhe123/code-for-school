package com.travel.mapper;

import com.travel.pojo.entity.ItineraryItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ItineraryMapper {
    List<ItineraryItem> listByUserId(@Param("userId") Long userId);

    ItineraryItem findById(@Param("id") Long id);

    ItineraryItem findByUserAndScenic(@Param("userId") Long userId, @Param("scenicId") Long scenicId);

    int insert(ItineraryItem item);

    int update(ItineraryItem item);

    int delete(@Param("id") Long id, @Param("userId") Long userId);

    int clear(@Param("userId") Long userId);

    Integer nextSortOrder(@Param("userId") Long userId);
}

