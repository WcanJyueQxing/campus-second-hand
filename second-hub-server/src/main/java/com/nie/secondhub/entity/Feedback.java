package com.nie.secondhub.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 反馈实体类
 */
@Data
@TableName("feedback")
public class Feedback {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 反馈内容
     */
    @TableField("content")
    private String content;

    /**
     * 联系方式
     */
    @TableField("contact")
    private String contact;

    /**
     * 状态：PENDING-待处理, REPLIED-已回复
     */
    @TableField("status")
    private String status;

    /**
     * 处理人ID
     */
    @TableField("handler_id")
    private Long handlerId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    /**
     * 是否删除：0-未删除，1-已删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Integer isDeleted;

    /**
     * 反馈类型：FEATURE-功能建议, BUG-Bug反馈, GOODS-商品问题, DISPUTE-交易纠纷, OTHER-其他
     */
    @TableField("type")
    private String type;
}