package com.nie.secondhub.service;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.user.CommentCreateRequest;
import com.nie.secondhub.dto.user.ReportCreateRequest;
import com.nie.secondhub.vo.CommentVO;
import com.nie.secondhub.vo.GoodsVO;

/**
 * 互动服务接口
 * 提供收藏、评论、举报等用户互动功能
 */
public interface InteractionService {

    /**
     * 添加收藏
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     */
    void favorite(Long userId, Long goodsId);

    /**
     * 取消收藏
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     */
    void unfavorite(Long userId, Long goodsId);

    /**
     * 切换收藏状态
     * 
     * @param userId 用户ID
     * @param goodsId 商品ID
     * @return 操作结果
     */
    ApiResponse<Void> toggleFavorite(Long userId, Long goodsId);

    /**
     * 查询用户收藏列表
     * 
     * @param userId 用户ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页收藏列表
     */
    PageResponse<GoodsVO> myFavorites(Long userId, Long pageNo, Long pageSize);

    /**
     * 添加评论
     * 
     * @param userId 用户ID
     * @param request 评论创建请求
     */
    void addComment(Long userId, CommentCreateRequest request);

    /**
     * 查询商品评论列表
     * 
     * @param goodsId 商品ID
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页评论列表
     */
    PageResponse<CommentVO> commentPage(Long goodsId, Long pageNo, Long pageSize);

    /**
     * 举报商品
     * 
     * @param userId 用户ID
     * @param request 举报创建请求
     */
    void report(Long userId, ReportCreateRequest request);

    /**
     * 分页查询举报列表（管理员）
     * 
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @param status 举报状态筛选
     * @return 分页举报列表
     */
    PageResponse<?> reportPage(Long pageNo, Long pageSize, String status);

    /**
     * 处理举报（管理员）
     * 
     * @param adminId 管理员ID
     * @param reportId 举报ID
     * @param request 处理请求
     */
    void handleReport(Long adminId, Long reportId, com.nie.secondhub.dto.admin.ReportHandleRequest request);
}