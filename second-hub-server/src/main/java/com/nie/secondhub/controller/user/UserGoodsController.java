package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.user.GoodsQueryRequest;
import com.nie.secondhub.dto.user.GoodsSaveRequest;
import com.nie.secondhub.service.GoodsService;
import com.nie.secondhub.vo.GoodsDetailVO;
import com.nie.secondhub.vo.GoodsVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户商品控制器
 * 提供用户发布、管理、查询商品的接口
 */
@Validated
@RestController
@RequestMapping("/api/user/goods")
public class UserGoodsController {

    @Resource
    private GoodsService goodsService;

    /**
     * 创建商品
     * 
     * @param request 商品保存请求
     * @return 创建的商品ID
     */
    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody GoodsSaveRequest request) {
        return ApiResponse.success(goodsService.createGoods(LoginUserHolder.requireUserId(), request));
    }

    /**
     * 更新商品
     * 
     * @param goodsId 商品ID
     * @param request 商品保存请求
     * @return 操作结果
     */
    @PutMapping("/{goodsId}")
    public ApiResponse<Void> update(@PathVariable Long goodsId, @Valid @RequestBody GoodsSaveRequest request) {
        goodsService.updateGoods(LoginUserHolder.requireUserId(), goodsId, request);
        return ApiResponse.success(null);
    }

    /**
     * 删除商品
     * 
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{goodsId}")
    public ApiResponse<Void> delete(@PathVariable Long goodsId) {
        goodsService.deleteGoods(LoginUserHolder.requireUserId(), goodsId);
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
        goodsService.offlineGoods(LoginUserHolder.requireUserId(), goodsId);
        return ApiResponse.success(null);
    }

    /**
     * 上架商品
     * 
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @PostMapping("/{goodsId}/online")
    public ApiResponse<Void> online(@PathVariable Long goodsId) {
        goodsService.onlineGoods(LoginUserHolder.requireUserId(), goodsId);
        return ApiResponse.success(null);
    }

    /**
     * 分页查询商品列表
     * 
     * @param request 查询请求（包含分类、关键词、排序等）
     * @return 分页商品列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResponse<GoodsVO>> list(@Valid GoodsQueryRequest request) {
        return ApiResponse.success(goodsService.userGoodsPage(request));
    }

    /**
     * 查询当前用户发布的商品列表
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页商品列表
     */
    @GetMapping("/my")
    public ApiResponse<PageResponse<GoodsVO>> myGoods(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                      @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(goodsService.myPublishedGoods(LoginUserHolder.requireUserId(), pageNo, pageSize));
    }

    /**
     * 查询商品详情
     * 
     * @param goodsId 商品ID
     * @return 商品详情
     */
    @GetMapping("/{goodsId}")
    public ApiResponse<GoodsDetailVO> detail(@PathVariable Long goodsId) {
        return ApiResponse.success(goodsService.goodsDetail(goodsId, LoginUserHolder.requireUserId()));
    }
}