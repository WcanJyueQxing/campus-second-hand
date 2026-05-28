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

/**
 * 浏览记录控制器
 * 提供浏览记录的添加、查询、删除接口
 */
@Validated
@RestController
@RequestMapping("/api/user/history")
public class HistoryRecordController {

    @Resource
    private HistoryRecordService historyRecordService;

    /**
     * 添加浏览记录
     * 
     * @param request 浏览记录请求（包含商品ID）
     * @return 操作结果
     */
    @PostMapping
    public ApiResponse<Void> addHistoryRecord(@Validated @RequestBody HistoryRecordRequest request) {
        Long userId = LoginUserHolder.requireUserId();
        historyRecordService.addHistoryRecord(userId, request);
        return ApiResponse.success(null);
    }

    /**
     * 获取浏览记录列表
     * 
     * @param limit 返回数量限制（默认50）
     * @return 浏览记录列表
     */
    @GetMapping
    public ApiResponse<List<HistoryRecordVO>> getHistoryRecords(@RequestParam(defaultValue = "50") Integer limit) {
        Long userId = LoginUserHolder.requireUserId();
        List<HistoryRecordVO> records = historyRecordService.getHistoryRecords(userId, limit);
        return ApiResponse.success(records);
    }

    /**
     * 删除单个浏览记录
     * 
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{goodsId}")
    public ApiResponse<Void> deleteHistoryRecord(@PathVariable Long goodsId) {
        Long userId = LoginUserHolder.requireUserId();
        historyRecordService.deleteHistoryRecord(userId, goodsId);
        return ApiResponse.success(null);
    }

    /**
     * 清空所有浏览记录
     * 
     * @return 操作结果
     */
    @DeleteMapping
    public ApiResponse<Void> clearHistoryRecords() {
        Long userId = LoginUserHolder.requireUserId();
        historyRecordService.clearHistoryRecords(userId);
        return ApiResponse.success(null);
    }
}