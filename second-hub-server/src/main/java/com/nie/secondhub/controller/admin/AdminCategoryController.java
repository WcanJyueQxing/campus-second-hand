package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.dto.admin.CategorySaveRequest;
import com.nie.secondhub.service.AdminOpsService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理控制器
 * 提供商品分类的增删改查接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Resource
    private AdminOpsService adminOpsService;

    /**
     * 获取所有分类列表
     * 
     * @return 分类列表
     */
    @GetMapping
    public ApiResponse<?> list() {
        return ApiResponse.success(adminOpsService.listCategory());
    }

    /**
     * 创建分类
     * 
     * @param request 分类保存请求
     * @return 创建的分类ID
     */
    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody CategorySaveRequest request) {
        return ApiResponse.success(adminOpsService.saveCategory(null, request));
    }

    /**
     * 更新分类
     * 
     * @param id 分类ID
     * @param request 分类保存请求
     * @return 更新后的分类ID
     */
    @PutMapping("/{id}")
    public ApiResponse<Long> update(@PathVariable Long id, @Valid @RequestBody CategorySaveRequest request) {
        return ApiResponse.success(adminOpsService.saveCategory(id, request));
    }

    /**
     * 删除分类
     * 
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        adminOpsService.deleteCategory(id);
        return ApiResponse.success(null);
    }
}