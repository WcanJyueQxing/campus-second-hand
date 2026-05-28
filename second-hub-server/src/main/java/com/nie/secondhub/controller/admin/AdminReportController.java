package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.ReportHandleRequest;
import com.nie.secondhub.service.InteractionService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 举报管理控制器
 * 提供举报列表查询和处理接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/reports")
public class AdminReportController {

    @Resource
    private InteractionService interactionService;

    /**
     * 分页查询举报列表
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @param status 举报状态筛选（可选）
     * @return 分页举报列表
     */
    @GetMapping
    public ApiResponse<PageResponse<?>> page(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                             @RequestParam(defaultValue = "10") @Min(1) Long pageSize,
                                             @RequestParam(required = false) String status) {
        return ApiResponse.success(interactionService.reportPage(pageNo, pageSize, status));
    }

    /**
     * 处理举报
     * 
     * @param reportId 举报ID
     * @param request 处理请求（包含处理结果和理由）
     * @return 操作结果
     */
    @PostMapping("/{reportId}/handle")
    public ApiResponse<Void> handle(@PathVariable Long reportId,
                                    @Valid @RequestBody ReportHandleRequest request) {
        interactionService.handleReport(LoginUserHolder.requireUserId(), reportId, request);
        return ApiResponse.success(null);
    }
}