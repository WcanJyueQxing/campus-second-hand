package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.UserStatusUpdateRequest;
import com.nie.secondhub.service.AdminOpsService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 * 提供用户列表查询、状态管理等管理员操作接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Resource
    private AdminOpsService adminOpsService;

    /**
     * 分页查询用户列表
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @param keyword 搜索关键词（支持用户名、手机号搜索）
     * @return 分页用户列表
     */
    @GetMapping
    public ApiResponse<PageResponse<?>> page(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                             @RequestParam(defaultValue = "10") @Min(1) Long pageSize,
                                             @RequestParam(required = false) String keyword) {
        return ApiResponse.success(adminOpsService.userPage(pageNo, pageSize, keyword));
    }

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param request 状态更新请求
     * @return 操作结果
     */
    @PostMapping("/{userId}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long userId,
                                          @Valid @RequestBody UserStatusUpdateRequest request) {
        adminOpsService.updateUserStatus(userId, request.getStatus());
        return ApiResponse.success(null);
    }
}