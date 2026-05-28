package com.nie.secondhub.common.enums;

/**
 * 反馈类型枚举
 */
public enum FeedbackType {
    
    /**
     * 功能建议
     */
    FEATURE("功能建议", "用户对功能的建议"),
    
    /**
     * Bug反馈
     */
    BUG("Bug反馈", "Bug问题反馈"),
    
    /**
     * 商品问题
     */
    GOODS("商品问题", "商品相关问题"),
    
    /**
     * 交易纠纷
     */
    DISPUTE("交易纠纷", "交易纠纷问题"),
    
    /**
     * 其他
     */
    OTHER("其他", "其他类型反馈");

    private final String name;
    private final String desc;

    FeedbackType(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * 根据枚举值获取中文名称
     * @param code 枚举值
     * @return 中文名称，找不到时返回原始值
     */
    public static String getNameByCode(String code) {
        for (FeedbackType type : FeedbackType.values()) {
            if (type.name().equals(code)) {
                return type.name;
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