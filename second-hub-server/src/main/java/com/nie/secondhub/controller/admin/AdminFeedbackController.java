package com.nie.secondhub.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nie.secondhub.dto.admin.FeedbackHandleRequest;
import com.nie.secondhub.service.FeedbackService;
import com.nie.secondhub.util.Result;
import com.nie.secondhub.vo.FeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员反馈管理控制器
 */
@RestController
@RequestMapping("/api/admin/feedback")
@RequiredArgsConstructor
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    /**
     * 分页查询反馈列表
     * @param page 页码（从1开始）
     * @param size 每页大小
     * @param status 状态筛选（可选）
     * @param type 类型筛选（可选）
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        
        IPage<FeedbackVO> result = feedbackService.getFeedbackList(page, size, status, type);
        return Result.success("查询成功", result);
    }

    /**
     * 查询单个反馈详情
     * @param id 反馈ID
     * @return 反馈详情
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        FeedbackVO feedback = feedbackService.getFeedbackById(id);
        if (feedback == null) {
            return Result.error("反馈不存在");
        }
        return Result.success("查询成功", feedback);
    }

    /**
     * 处理反馈（标记为已回复）
     * @param id 反馈ID
     * @param request 处理请求
     * @return 是否成功
     */
    @PutMapping("/{id}/handle")
    public Result handle(@PathVariable Long id, @RequestBody FeedbackHandleRequest request) {
        boolean success = feedbackService.handleFeedback(id, request.getHandlerId());
        if (!success) {
            return Result.error("处理失败");
        }
        return Result.success("处理成功");
    }

    /**
     * 删除反馈
     * @param id 反馈ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        boolean success = feedbackService.deleteFeedback(id);
        if (!success) {
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    /**
     * 获取待处理反馈数量
     * @return 待处理数量
     */
    @GetMapping("/pending-count")
    public Result getPendingCount() {
        long count = feedbackService.getPendingCount();
        return Result.success("查询成功", count);
    }
}