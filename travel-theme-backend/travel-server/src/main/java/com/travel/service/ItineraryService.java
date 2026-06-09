package com.travel.service;

import com.travel.common.error.BusinessException;
import com.travel.common.error.ErrorCode;
import com.travel.interceptor.UserContext;
import com.travel.mapper.ItineraryMapper;
import com.travel.mapper.ScenicMapper;
import com.travel.pojo.dto.ItineraryAddDTO;
import com.travel.pojo.dto.ItineraryUpdateDTO;
import com.travel.pojo.entity.ItineraryItem;
import com.travel.pojo.entity.ScenicSpot;
import com.travel.pojo.vo.ItineraryItemVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryService {
    private final ItineraryMapper itineraryMapper;
    private final ScenicMapper scenicMapper;

    public ItineraryService(ItineraryMapper itineraryMapper, ScenicMapper scenicMapper) {
        this.itineraryMapper = itineraryMapper;
        this.scenicMapper = scenicMapper;
    }

    public List<ItineraryItemVO> listMine() {
        Long userId = requireUserId();
        List<ItineraryItem> items = itineraryMapper.listByUserId(userId);
        List<ItineraryItemVO> result = new ArrayList<>();
        for (ItineraryItem item : items) {
            ScenicSpot spot = scenicMapper.findById(item.getScenicId());
            ItineraryItemVO vo = new ItineraryItemVO();
            vo.setId(item.getId());
            vo.setScenicId(item.getScenicId());
            vo.setNote(item.getNote());
            vo.setSortOrder(item.getSortOrder());
            if (spot != null) {
                vo.setName(spot.getName());
                vo.setPrice(spot.getPrice());
            }
            result.add(vo);
        }
        return result;
    }

    public ItineraryItemVO add(ItineraryAddDTO dto) {
        Long userId = requireUserId();
        if (dto == null || dto.getScenicId() == null) {
            throw new IllegalArgumentException("scenicId 不能为空");
        }
        ScenicSpot spot = scenicMapper.findById(dto.getScenicId());
        if (spot == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "景点不存在");
        }

        ItineraryItem exists = itineraryMapper.findByUserAndScenic(userId, dto.getScenicId());
        if (exists != null) {
            exists.setNote(dto.getNote());
            exists.setUpdatedAt(LocalDateTime.now());
            itineraryMapper.update(exists);
            return toVO(exists, spot);
        }

        Integer nextSort = itineraryMapper.nextSortOrder(userId);
        ItineraryItem item = new ItineraryItem();
        item.setUserId(userId);
        item.setScenicId(dto.getScenicId());
        item.setNote(dto.getNote());
        item.setSortOrder(nextSort == null ? 1 : nextSort);
        item.setCreatedAt(LocalDateTime.now());
        item.setUpdatedAt(LocalDateTime.now());
        itineraryMapper.insert(item);
        return toVO(item, spot);
    }

    public void update(Long id, ItineraryUpdateDTO dto) {
        Long userId = requireUserId();
        ItineraryItem item = itineraryMapper.findById(id);
        if (item == null || !userId.equals(item.getUserId())) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "行程项不存在");
        }
        if (dto == null) throw new IllegalArgumentException("请求体不能为空");
        if (dto.getNote() != null) item.setNote(dto.getNote());
        if (dto.getSortOrder() != null) item.setSortOrder(dto.getSortOrder());
        item.setUpdatedAt(LocalDateTime.now());
        itineraryMapper.update(item);
    }

    public void remove(Long id) {
        Long userId = requireUserId();
        itineraryMapper.delete(id, userId);
    }

    public void clear() {
        Long userId = requireUserId();
        itineraryMapper.clear(userId);
    }

    private Long requireUserId() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "未登录");
        }
        return userId;
    }

    private ItineraryItemVO toVO(ItineraryItem item, ScenicSpot spot) {
        ItineraryItemVO vo = new ItineraryItemVO();
        vo.setId(item.getId());
        vo.setScenicId(item.getScenicId());
        vo.setNote(item.getNote());
        vo.setSortOrder(item.getSortOrder());
        vo.setName(spot.getName());
        vo.setPrice(spot.getPrice());
        return vo;
    }
}

