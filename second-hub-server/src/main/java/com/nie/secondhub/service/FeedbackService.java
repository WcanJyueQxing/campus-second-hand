package com.nie.secondhub.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.nie.secondhub.entity.Feedback;
import com.nie.secondhub.vo.FeedbackVO;

/**
 * 反馈服务接口
 */
public interface FeedbackService extends IService<Feedback> {

    /**
     * 分页查询反馈列表
     * @param page 页码
     * @param size 每页大小
     * @param status 状态筛选（可选）
     * @param type 类型筛选（可选）
     * @return 分页结果
     */
    IPage<FeedbackVO> getFeedbackList(int page, int size, String status, String type);

    /**
     * 根据ID查询反馈详情
     * @param id 反馈ID
     * @return 反馈详情
     */
    FeedbackVO getFeedbackById(Long id);

    /**
     * 处理反馈（标记为已回复）
     * @param id 反馈ID
     * @param handlerId 处理人ID
     * @return 是否成功
     */
    boolean handleFeedback(Long id, Long handlerId);

    /**
     * 删除反馈（逻辑删除）
     * @param id 反馈ID
     * @return 是否成功
     */
    boolean deleteFeedback(Long id);

    /**
     * 获取待处理反馈数量
     * @return 待处理数量
     */
    long getPendingCount();
}