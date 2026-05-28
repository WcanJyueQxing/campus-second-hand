package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.service.OrderService;
import com.nie.secondhub.vo.OrderVO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理控制器
 * 提供订单查询、取消等管理员操作接口
 */
@Validated
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Resource
    private OrderService orderService;

    /**
     * 分页查询订单列表（支持搜索）
     * 
     * @param pageNo 页码（从1开始）
     * @param pageSize 每页大小
     * @param orderStatus 订单状态筛选（可选）
     * @param keyword 搜索关键词（订单号）
     * @return 分页订单列表
     */
    @GetMapping
    public ApiResponse<PageResponse<OrderVO>> page(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                   @RequestParam(defaultValue = "10") @Min(1) Long pageSize,
                                                   @RequestParam(required = false) String orderStatus,
                                                   @RequestParam(required = false) String keyword) {
        return ApiResponse.success(orderService.adminOrders(pageNo, pageSize, orderStatus, keyword));
    }

    /**
     * 管理员取消订单
     * 
     * @param orderId 订单ID
     * @return 操作结果
     */
    @PostMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long orderId) {
        orderService.adminCancel(orderId);
        return ApiResponse.success(null);
    }
}