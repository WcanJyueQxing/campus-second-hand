package com.nie.secondhub.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nie.secondhub.dto.user.HistoryRecordRequest;
import com.nie.secondhub.entity.HistoryRecord;
import com.nie.secondhub.mapper.HistoryRecordMapper;
import com.nie.secondhub.service.HistoryRecordService;
import com.nie.secondhub.vo.HistoryRecordVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryRecordServiceImpl implements HistoryRecordService {

    @Resource
    private HistoryRecordMapper historyRecordMapper;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public void addHistoryRecord(Long userId, HistoryRecordRequest request) {
        // 先删除已存在的记录，避免重复
        historyRecordMapper.deleteByUserIdAndGoodsId(userId, request.getGoodsId());

        // 创建新的历史记录
        HistoryRecord record = new HistoryRecord();
        record.setUserId(userId);
        record.setGoodsId(request.getGoodsId());
        record.setTitle(request.getTitle());
        record.setPrice(request.getPrice());
        try {
            record.setImages(objectMapper.writeValueAsString(request.getImages()));
        } catch (JsonProcessingException e) {
            record.setImages("[]");
        }
        record.setViewTime(LocalDateTime.now());
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());

        historyRecordMapper.insert(record);
    }

    @Override
    public List<HistoryRecordVO> getHistoryRecords(Long userId, Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 50;
        }
        List<HistoryRecord> records = historyRecordMapper.selectByUserIdOrderByViewTimeDesc(userId, limit);
        return records.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public void deleteHistoryRecord(Long userId, Long goodsId) {
        historyRecordMapper.deleteByUserIdAndGoodsId(userId, goodsId);
    }

    @Override
    public void clearHistoryRecords(Long userId) {
        historyRecordMapper.deleteByUserId(userId);
    }

    private HistoryRecordVO convertToVO(HistoryRecord record) {
        HistoryRecordVO vo = new HistoryRecordVO();
        vo.setId(record.getId());
        vo.setGoodsId(record.getGoodsId());
        vo.setTitle(record.getTitle());
        vo.setPrice(record.getPrice());
        try {
            vo.setImages(objectMapper.readValue(record.getImages(), String[].class));
        } catch (JsonProcessingException e) {
            vo.setImages(new String[0]);
        }
        vo.setViewTime(record.getViewTime());
        return vo;
    }
}