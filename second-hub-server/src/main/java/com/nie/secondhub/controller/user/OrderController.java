package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.service.OrderService;
import com.nie.secondhub.vo.OrderVO;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("/sell")
    public ApiResponse<PageResponse<OrderVO>> mySoldOrders(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                            @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(orderService.myOrders(LoginUserHolder.requireUserId(), "seller", pageNo, pageSize));
    }

    @GetMapping("/bought")
    public ApiResponse<PageResponse<OrderVO>> myBoughtOrders(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                             @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(orderService.myOrders(LoginUserHolder.requireUserId(), "buyer", pageNo, pageSize));
    }

    @GetMapping("/comment")
    public ApiResponse<PageResponse<OrderVO>> pendingReviews(@RequestParam(defaultValue = "1") @Min(1) Long pageNo,
                                                            @RequestParam(defaultValue = "10") @Min(1) Long pageSize) {
        return ApiResponse.success(orderService.myOrders(LoginUserHolder.requireUserId(), "buyer", pageNo, pageSize));
    }
}