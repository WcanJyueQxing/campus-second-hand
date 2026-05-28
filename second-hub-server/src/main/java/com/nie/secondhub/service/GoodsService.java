package com.nie.secondhub.service;

import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.user.GoodsQueryRequest;
import com.nie.secondhub.dto.user.GoodsSaveRequest;
import com.nie.secondhub.vo.GoodsDetailVO;
import com.nie.secondhub.vo.GoodsVO;

/**
 * 商品服务接口
 * 提供商品的创建、更新、删除、查询等功能
 */
public interface GoodsService {

    /**
     * 创建商品
     * 
     * @param userId 用户ID
     * @param request 商品保存请求
     * @return 创建的商品ID
     */
    Long createGoods(Long userId, GoodsSaveRequest request);

    /**
     * 更新商品
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @param request 商品保存请求
     */
    void updateGoods(Long userId, Long goodsId, GoodsSaveRequest request);

    /**
     * 删除商品
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     */
    void deleteGoods(Long userId, Long goodsId);

    /**
     * 下架商品
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     */
    void offlineGoods(Long userId, Long goodsId);
    
    /**
     * 上架商品
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     */
    void onlineGoods(Long userId, Long goodsId);

    /**
     * 获取商品详情
     * 
     * @param goodsId 商品ID
     * @param currentUserId 当前用户ID
     * @return 商品详情
     */
    GoodsDetailVO goodsDetail(Long goodsId, Long currentUserId);

    /**
     * 分页查询商品列表
     * 
     * @param request 查询请求
     * @return 分页商品列表
     */
    PageResponse<GoodsVO> userGoodsPage(GoodsQueryRequest request);

    /**
     * 查询用户发布的商品列表
     * 
     * @param userId 用户ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页商品列表
     */
    PageResponse<GoodsVO> myPublishedGoods(Long userId, Long pageNo, Long pageSize);

    /**
     * 查询待审核商品列表（管理员）
     * 
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页商品列表
     */
    PageResponse<GoodsVO> pendingGoods(Long pageNo, Long pageSize);

    /**
     * 审核商品（管理员）
     * 
     * @param adminId 管理员ID
     * @param goodsId 商品ID
     * @param approved 是否通过
     * @param reason 审核理由
     */
    void auditGoods(Long adminId, Long goodsId, boolean approved, String reason);

    /**
     * 管理员下架商品
     * 
     * @param goodsId 商品ID
     */
    void adminOfflineGoods(Long goodsId);

    /**
     * 管理员分页查询商品列表（支持搜索）
     * 
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键词（标题模糊匹配）
     * @return 分页商品列表
     */
    PageResponse<GoodsVO> adminGoodsPage(Long pageNo, Long pageSize, String keyword);

    /**
     * 管理员删除商品
     * 
     * @param goodsId 商品ID
     */
    void adminDeleteGoods(Long goodsId);
}