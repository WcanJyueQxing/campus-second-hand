package com.nie.secondhub.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 反馈 VO
 */
@Data
public class FeedbackVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 状态（中文）
     */
    private String status;

    /**
     * 原始状态值
     */
    private String statusCode;

    /**
     * 处理人ID
     */
    private Long handlerId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 反馈类型（中文）
     */
    private String type;

    /**
     * 原始类型值
     */
    private String typeCode;
}