package com.nie.secondhub.common.enums;

public enum PayStatus {
    UNPAID("未支付", "订单未支付"),
    PAID("已支付", "订单已支付");

    private final String name;
    private final String desc;

    PayStatus(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static String getNameByCode(String code) {
        for (PayStatus status : PayStatus.values()) {
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
