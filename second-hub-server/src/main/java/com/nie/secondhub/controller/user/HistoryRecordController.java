package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.dto.user.HistoryRecordRequest;
import com.nie.secondhub.service.HistoryRecordService;
import com.nie.secondhub.vo.HistoryRecordVO;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/user/history")
public class HistoryRecordController {

    @Resource
    private HistoryRecordService historyRecordService;

    @PostMapping
    public ApiResponse<Void> addHistoryRecord(@Validated @RequestBody HistoryRecordRequest request) {
        Long userId = LoginUserHolder.requireUserId();
        historyRecordService.addHistoryRecord(userId, request);
        return ApiResponse.success(null);
    }

    @GetMapping
    public ApiResponse<List<HistoryRecordVO>> getHistoryRecords(@RequestParam(defaultValue = "50") Integer limit) {
        Long userId = LoginUserHolder.requireUserId();
        List<HistoryRecordVO> records = historyRecordService.getHistoryRecords(userId, limit);
        return ApiResponse.success(records);
    }

    @DeleteMapping("/{goodsId}")
    public ApiResponse<Void> deleteHistoryRecord(@PathVariable Long goodsId) {
        Long userId = LoginUserHolder.requireUserId();
        historyRecordService.deleteHistoryRecord(userId, goodsId);
        return ApiResponse.success(null);
    }

    @DeleteMapping
    public ApiResponse<Void> clearHistoryRecords() {
        Long userId = LoginUserHolder.requireUserId();
        historyRecordService.clearHistoryRecords(userId);
        return ApiResponse.success(null);
    }
}