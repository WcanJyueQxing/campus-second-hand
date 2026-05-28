package com.nie.secondhub.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nie.secondhub.common.enums.FeedbackStatus;
import com.nie.secondhub.common.enums.FeedbackType;
import com.nie.secondhub.entity.Feedback;
import com.nie.secondhub.entity.User;
import com.nie.secondhub.entity.UserProfile;
import com.nie.secondhub.mapper.FeedbackMapper;
import com.nie.secondhub.mapper.UserMapper;
import com.nie.secondhub.mapper.UserProfileMapper;
import com.nie.secondhub.service.FeedbackService;
import com.nie.secondhub.vo.FeedbackVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 反馈服务实现
 */
@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl extends ServiceImpl<FeedbackMapper, Feedback> implements FeedbackService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public IPage<FeedbackVO> getFeedbackList(int page, int size, String status, String type) {
        LambdaQueryWrapper<Feedback> queryWrapper = Wrappers.lambdaQuery();
        
        // 状态筛选
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq(Feedback::getStatus, status);
        }
        
        // 类型筛选
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq(Feedback::getType, type);
        }
        
        // 按创建时间倒序
        queryWrapper.orderByDesc(Feedback::getCreatedAt);
        
        IPage<Feedback> feedbackPage = this.page(new Page<>(page, size), queryWrapper);
        
        return feedbackPage.convert(this::toVO);
    }

    @Override
    public FeedbackVO getFeedbackById(Long id) {
        Feedback feedback = this.getById(id);
        return feedback != null ? toVO(feedback) : null;
    }

    @Override
    public boolean handleFeedback(Long id, Long handlerId) {
        Feedback feedback = this.getById(id);
        if (feedback == null) {
            return false;
        }
        
        feedback.setStatus(FeedbackStatus.REPLIED.name());
        feedback.setHandlerId(handlerId);
        
        return this.updateById(feedback);
    }

    @Override
    public boolean deleteFeedback(Long id) {
        return this.removeById(id);
    }

    @Override
    public long getPendingCount() {
        return this.count(Wrappers.lambdaQuery(Feedback.class)
                .eq(Feedback::getStatus, FeedbackStatus.PENDING.name()));
    }

    /**
     * 转换为 VO
     */
    private FeedbackVO toVO(Feedback feedback) {
        FeedbackVO vo = new FeedbackVO();
        vo.setId(feedback.getId());
        vo.setUserId(feedback.getUserId());
        vo.setContent(feedback.getContent());
        vo.setContact(feedback.getContact());
        vo.setStatus(FeedbackStatus.getNameByCode(feedback.getStatus()));
        vo.setStatusCode(feedback.getStatus());
        vo.setHandlerId(feedback.getHandlerId());
        vo.setCreatedAt(feedback.getCreatedAt());
        vo.setUpdatedAt(feedback.getUpdatedAt());
        vo.setType(FeedbackType.getNameByCode(feedback.getType()));
        vo.setTypeCode(feedback.getType());
        
        // 查询用户信息
        Optional.ofNullable(userMapper.selectById(feedback.getUserId()))
                .ifPresent(user -> vo.setUserNickname(user.getNickname()));
        
        // 查询用户头像
        Optional.ofNullable(userProfileMapper.selectById(feedback.getUserId()))
                .ifPresent(profile -> vo.setUserAvatar(profile.getAvatarUrl()));
        
        return vo;
    }
}