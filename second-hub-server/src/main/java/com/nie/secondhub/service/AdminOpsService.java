package com.nie.secondhub.service;

import com.nie.secondhub.common.response.PageResponse;
import com.nie.secondhub.dto.admin.CategorySaveRequest;
import com.nie.secondhub.dto.admin.NoticeSaveRequest;
import com.nie.secondhub.vo.DashboardVO;

import java.util.List;
import java.util.Map;

/**
 * 管理员操作服务接口
 * 提供分类管理、用户管理、公告管理、仪表盘等管理员功能
 */
public interface AdminOpsService {

    /**
     * 保存分类（新增或更新）
     * 
     * @param id 分类ID（新增时为null）
     * @param request 分类保存请求
     * @return 分类ID
     */
    Long saveCategory(Long id, CategorySaveRequest request);

    /**
     * 删除分类
     * 
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 获取所有分类列表
     * 
     * @return 分类列表
     */
    List<?> listCategory();

    /**
     * 分页查询用户列表
     * 
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @param keyword 搜索关键词
     * @return 分页用户列表
     */
    PageResponse<?> userPage(Long pageNo, Long pageSize, String keyword);

    /**
     * 更新用户状态
     * 
     * @param userId 用户ID
     * @param status 状态值
     */
    void updateUserStatus(Long userId, Integer status);

    /**
     * 保存公告（新增或更新）
     * 
     * @param adminId 管理员ID
     * @param id 公告ID（新增时为null）
     * @param request 公告保存请求
     * @return 公告ID
     */
    Long saveNotice(Long adminId, Long id, NoticeSaveRequest request);

    /**
     * 删除公告
     * 
     * @param id 公告ID
     */
    void deleteNotice(Long id);

    /**
     * 分页查询公告列表
     * 
     * @param pageNo 页码
     * @param pageSize 每页大小
     * @return 分页公告列表
     */
    PageResponse<?> noticePage(Long pageNo, Long pageSize);

    /**
     * 获取仪表盘概览数据
     * 
     * @return 仪表盘数据
     */
    DashboardVO dashboardOverview();

    /**
     * 获取用户、商品、订单趋势数据
     * 
     * @param timeRange 时间范围
     * @return 趋势数据列表
     */
    List<Map<String, Object>> userGoodsOrderTrend(String timeRange);
}