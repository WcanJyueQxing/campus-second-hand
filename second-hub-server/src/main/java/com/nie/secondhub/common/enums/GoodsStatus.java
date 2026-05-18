package com.nie.secondhub.common.enums;

public enum GoodsStatus {
    DRAFT("草稿", "商品草稿状态"),
    PENDING("待审核", "商品正在审核中"),
    APPROVED("已上架", "审核通过，正常售卖"),
    REJECTED("已驳回", "商品审核未通过"),
    OFFLINE("已下架", "商品已下架"),
    SOLD("已售出", "商品已被购买");

    private final String name;
    private final String desc;

    GoodsStatus(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static String getNameByCode(String code) {
        for (GoodsStatus status : GoodsStatus.values()) {
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
