package com.travel.service;

import com.travel.common.error.BusinessException;
import com.travel.common.error.ErrorCode;
import com.travel.mapper.ScenicMapper;
import com.travel.pojo.dto.ScenicUpsertDTO;
import com.travel.pojo.entity.ScenicSpot;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScenicService {
    private final ScenicMapper scenicMapper;

    public ScenicService(ScenicMapper scenicMapper) {
        this.scenicMapper = scenicMapper;
    }

    public List<ScenicSpot> list(String keyword, String category) {
        return scenicMapper.search(keyword, category);
    }

    public ScenicSpot detail(Long id) {
        ScenicSpot spot = scenicMapper.findById(id);
        if (spot == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "景点不存在");
        }
        return spot;
    }

    public ScenicSpot create(ScenicUpsertDTO dto) {
        ScenicSpot spot = new ScenicSpot();
        apply(dto, spot);
        spot.setCreatedAt(LocalDateTime.now());
        spot.setUpdatedAt(LocalDateTime.now());
        scenicMapper.insert(spot);
        return scenicMapper.findById(spot.getId());
    }

    public ScenicSpot update(Long id, ScenicUpsertDTO dto) {
        ScenicSpot existing = scenicMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "景点不存在");
        }
        apply(dto, existing);
        existing.setId(id);
        existing.setUpdatedAt(LocalDateTime.now());
        scenicMapper.update(existing);
        return scenicMapper.findById(id);
    }

    public void remove(Long id) {
        scenicMapper.delete(id);
    }

    private void apply(ScenicUpsertDTO dto, ScenicSpot spot) {
        if (dto == null) throw new IllegalArgumentException("请求体不能为空");
        if (dto.getName() == null || dto.getName().isBlank()) throw new IllegalArgumentException("name 不能为空");
        if (dto.getCategory() == null || dto.getCategory().isBlank()) throw new IllegalArgumentException("category 不能为空");
        if (dto.getPrice() == null || dto.getPrice() < 0) throw new IllegalArgumentException("price 非法");
        spot.setName(dto.getName().trim());
        spot.setCategory(dto.getCategory().trim());
        spot.setPrice(dto.getPrice());
        spot.setAddress(dto.getAddress());
        spot.setImage(dto.getImage());
        spot.setDescription(dto.getDescription());
    }
}

