package com.nie.secondhub.service;

import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.user.OrderCreateRequest;
import com.nie.secondhub.vo.OrderVO;

/**
 * 订单服务接口
 * 提供订单的创建、支付、确认、取消等功能
 */
public interface OrderService {

    /**
     * 创建订单
     * 
     * @param buyerId 买家ID
     * @param request 订单创建请求
     * @return 创建的订单ID
     */
    Long createOrder(Long buyerId, OrderCreateRequest request);

    /**
     * 订单支付
     * 
     * @param buyerId 买家ID
     * @param orderId 订单ID
     */
    void pay(Long buyerId, Long orderId);

    /**
     * 卖家确认订单
     * 
     * @param sellerId 卖家ID
     * @param orderId 订单ID
     */
    void sellerConfirm(Long sellerId, Long orderId);

    /**
     * 买家确认收货
     * 
     * @param buyerId 买家ID
     * @param orderId 订单ID
     */
    void buyerConfirm(Long buyerId, Long orderId);

    /**
     * 取消订单
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     */
    void cancel(Long userId, Long orderId);

    /**
     * 获取订单详情
     * 
     * @param userId 用户ID
     * @param orderId 订单ID
     * @return 订单详情
     */
    OrderVO detail(Long userId, Long orderId);

    /**
     * 查询用户订单列表
     * 
     * @param userId 用户ID
     * @param asRole 角色（buyer-买家, seller-卖家）
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页订单列表
     */
    PageResponse<OrderVO> myOrders(Long userId, String asRole, Long pageNo, Long pageSize);

    /**
     * 管理员查询订单列表（支持搜索）
     * 
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @param orderStatus 订单状态筛选
     * @param keyword 搜索关键词（订单号或商品标题）
     * @return 分页订单列表
     */
    PageResponse<OrderVO> adminOrders(Long pageNo, Long pageSize, String orderStatus, String keyword);

    /**
     * 管理员取消订单
     * 
     * @param orderId 订单ID
     */
    void adminCancel(Long orderId);

    /**
     * 统计卖家订单数量
     * 
     * @param sellerId 卖家ID
     * @return 订单数量
     */
    Long countSellerOrders(Long sellerId);

    /**
     * 统计买家订单数量
     * 
     * @param buyerId 买家ID
     * @return 订单数量
     */
    Long countBuyerOrders(Long buyerId);

    /**
     * 统计待评价订单数量
     * 
     * @param buyerId 买家ID
     * @return 待评价订单数量
     */
    Long countPendingReviewOrders(Long buyerId);
}