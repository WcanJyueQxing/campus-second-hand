package com.nie.secondhub.controller.admin;

import com.nie.secondhub.common.response.ApiResponse;
import com.nie.secondhub.service.AdminOpsService;
import com.nie.secondhub.vo.DashboardVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘控制器
 * 提供后台首页数据统计接口
 */
@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    @Resource
    private AdminOpsService adminOpsService;

    /**
     * 获取仪表盘概览数据
     * 
     * @return 仪表盘数据（商品、订单、用户统计）
     */
    @GetMapping("/overview")
    public ApiResponse<DashboardVO> overview() {
        return ApiResponse.success(adminOpsService.dashboardOverview());
    }

    /**
     * 获取趋势数据
     * 
     * @param timeRange 时间范围（默认7days）
     * @return 用户、商品、订单趋势数据
     */
    @GetMapping("/trend")
    public ApiResponse<?> trend(@RequestParam(value = "timeRange", defaultValue = "7days") String timeRange) {
        return ApiResponse.success(adminOpsService.userGoodsOrderTrend(timeRange));
    }
}