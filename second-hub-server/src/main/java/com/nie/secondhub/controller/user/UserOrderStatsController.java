package com.nie.secondhub.controller.user;

import com.nie.secondhub.common.context.LoginUserHolder;
import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.service.OrderService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/orders")
public class UserOrderStatsController {

    @Resource
    private OrderService orderService;

    @GetMapping("/stats")
    public ApiResponse<OrderStatsVO> getOrderStats() {
        Long userId = LoginUserHolder.requireUserId();
        OrderStatsVO stats = new OrderStatsVO();
        stats.setSoldCount(orderService.countSellerOrders(userId));
        stats.setBoughtCount(orderService.countBuyerOrders(userId));
        stats.setPendingReviewCount(orderService.countPendingReviewOrders(userId));
        return ApiResponse.success(stats);
    }

    public static class OrderStatsVO {
        private Long soldCount;
        private Long boughtCount;
        private Long pendingReviewCount;

        public Long getSoldCount() {
            return soldCount;
        }

        public void setSoldCount(Long soldCount) {
            this.soldCount = soldCount;
        }

        public Long getBoughtCount() {
            return boughtCount;
        }

        public void setBoughtCount(Long boughtCount) {
            this.boughtCount = boughtCount;
        }

        public Long getPendingReviewCount() {
            return pendingReviewCount;
        }

        public void setPendingReviewCount(Long pendingReviewCount) {
            this.pendingReviewCount = pendingReviewCount;
        }
    }
}