package com.nie.secondhub.common.enums;

/**
 * 反馈状态枚举
 */
public enum FeedbackStatus {
    
    /**
     * 待处理
     */
    PENDING("待处理", "等待管理员处理"),
    
    /**
     * 已回复
     */
    REPLIED("已回复", "管理员已回复");

    private final String name;
    private final String desc;

    FeedbackStatus(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * 根据枚举值获取中文名称
     * @param code 枚举值
     * @return 中文名称，找不到时返回原始值
     */
    public static String getNameByCode(String code) {
        for (FeedbackStatus status : FeedbackStatus.values()) {
            if (status.name().equals(code)) {
                return status.name;
            }
        }
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }
}