package com.nie.secondhub.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nie.secondhub.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 反馈 Mapper 接口
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}