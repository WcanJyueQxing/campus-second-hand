package com.nie.secondhub.dto.admin;

import lombok.Data;

/**
 * 反馈处理请求 DTO
 */
@Data
public class FeedbackHandleRequest {

    /**
     * 处理人ID
     */
    private Long handlerId;
}