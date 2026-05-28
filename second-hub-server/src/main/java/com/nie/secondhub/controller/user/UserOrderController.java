package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.user.OrderCreateRequest;
import com.nie.secondhub.service.OrderService;
import com.nie.secondhub.vo.OrderVO;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户订单控制器
 * 提供订单创建、支付、确认、取消等接口
 */
@Validated
@RestController
@RequestMapping("/api/user/orders")
public class UserOrderController {

    @Resource
    private OrderService orderService;

    /**
     * 创建订单
     * 
     * @param request 订单创建请求（包含商品ID、数量等）
     * @return 创建的订单ID
     */
    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.success(orderService.createOrder(LoginUserHolder.requireUserId(), request));
    }

    /**
     * 订单支付
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/{orderId}/pay")
    public ApiResponse<Void> pay(@PathVariable Long orderId) {
        orderService.pay(LoginUserHolder.requireUserId(), orderId);
        return ApiResponse.success(null);
    }

    /**
     * 卖家确认订单
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/{orderId}/seller-confirm")
    public ApiResponse<Void> sellerConfirm(@PathVariable Long orderId) {
        orderService.sellerConfirm(LoginUserHolder.requireUserId(), orderId);
        return ApiResponse.success(null);
    }

    /**
     * 买家确认收货
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/{orderId}/buyer-confirm")
    public ApiResponse<Void> buyerConfirm(@PathVariable Long orderId) {
        orderService.buyerConfirm(LoginUserHolder.requireUserId(), orderId);
        return ApiResponse.success(null);
    }

    /**
     * 取消订单
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long orderId) {
        orderService.cancel(LoginUserHolder.requireUserId(), orderId);
        return ApiResponse.success(null);
    }

    /**
     * 查询订单详情
     * 
     * @param orderId 订单ID
     * @return 订单详情
     */
    @GetMapping("/{orderId}")
    public ApiResponse<OrderVO> detail(@PathVariable Long orderId) {
        return ApiResponse.success(orderService.detail(LoginUserHolder.requireUserId(), orderId));
    }

    /**
     * 查询当前用户的订单列表
     * 
     * @param asRole 角色（buyer-买家, seller-卖家）
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @return 分页订单列表
     */
    @GetMapping("/my")
    public ApiResponse<PageResponse<OrderVO>> myOrders(@RequestParam(defaultValue = "buyer") String asRole,
                                                       @RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                       @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(orderService.myOrders(LoginUserHolder.requireUserId(), asRole, pageNo, pageSize));
    }
}