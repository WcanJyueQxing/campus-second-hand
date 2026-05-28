package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.GoodsAuditRequest;
import com.nie.secondhub.service.GoodsService;
import com.nie.secondhub.vo.GoodsVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 商品管理控制器
 * 提供商品审核、上下架、查询等管理员操作接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/goods")
public class AdminGoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 分页查询待审核商品列表
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页商品列表
     */
    @GetMapping("/pending")
    public ApiResponse<PageResponse<GoodsVO>> pending(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                      @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(goodsService.pendingGoods(pageNo, pageSize));
    }

    /**
     * 审核商品
     * 
     * @param goodsId 商品ID
     * @param request 审核请求（包含审核结果和拒绝理由）
     * @return 操作结果
     */
    @PostMapping("/{goodsId}/audit")
    public ApiResponse<Void> audit(@PathVariable Long goodsId, @Valid @RequestBody GoodsAuditRequest request) {
        goodsService.auditGoods(LoginUserHolder.requireUserId(), goodsId, request.getApproved(), request.getReason());
        return ApiResponse.success(null);
    }

    /**
     * 下架商品
     * 
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @PostMapping("/{goodsId}/offline")
    public ApiResponse<Void> offline(@PathVariable Long goodsId) {
        goodsService.adminOfflineGoods(goodsId);
        return ApiResponse.success(null);
    }

    /**
     * 分页查询所有商品列表（支持搜索）
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @param keyword 搜索关键词（标题）
     * @return 分页商品列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<GoodsVO>> list(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                   @RequestParam(defaultValue = "10") @Min(1) Long pageSize,
                                                   @RequestParam(required = false) String keyword) {
        return ApiResponse.success(goodsService.adminGoodsPage(pageNo, pageSize, keyword));
    }

    /**
     * 删除商品
     * 
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{goodsId}")
    public ApiResponse<Void> delete(@PathVariable Long goodsId) {
        goodsService.adminDeleteGoods(goodsId);
        return ApiResponse.success(null);
    }
}