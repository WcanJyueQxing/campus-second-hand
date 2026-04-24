package com.nie.secondhub.service;

import com.nie.secondhub.dto.user.HistoryRecordRequest;
import com.nie.secondhub.vo.HistoryRecordVO;

import java.util.List;

public interface HistoryRecordService {
    void addHistoryRecord(Long userId, HistoryRecordRequest request);
    List<HistoryRecordVO> getHistoryRecords(Long userId, Integer limit);
    void deleteHistoryRecord(Long userId, Long goodsId);
    void clearHistoryRecords(Long userId);
}