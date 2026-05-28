package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.NoticeSaveRequest;
import com.nie.secondhub.service.AdminOpsService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公告管理控制器
 * 提供公告的增删改查接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/notices")
public class AdminNoticeController {

    @Resource
    private AdminOpsService adminOpsService;

    /**
     * 分页查询公告列表
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页公告列表
     */
    @GetMapping
    public ApiResponse<PageResponse<?>> page(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                             @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(adminOpsService.noticePage(pageNo, pageSize));
    }

    /**
     * 创建公告
     * 
     * @param request 公告保存请求
     * @return 创建的公告ID
     */
    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody NoticeSaveRequest request) {
        return ApiResponse.success(adminOpsService.saveNotice(LoginUserHolder.requireUserId(), null, request));
    }

    /**
     * 更新公告
     * 
     * @param id 公告ID
     * @param request 公告保存请求
     * @return 更新后的公告ID
     */
    @PutMapping("/{id}")
    public ApiResponse<Long> update(@PathVariable Long id, @Valid @RequestBody NoticeSaveRequest request) {
        return ApiResponse.success(adminOpsService.saveNotice(LoginUserHolder.requireUserId(), id, request));
    }

    /**
     * 删除公告
     * 
     * @param id 公告ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        adminOpsService.deleteNotice(id);
        return ApiResponse.success(null);
    }
}