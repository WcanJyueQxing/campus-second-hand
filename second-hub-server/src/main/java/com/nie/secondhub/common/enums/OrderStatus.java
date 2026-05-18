package com.nie.secondhub.common.enums;

public enum OrderStatus {
    PENDING_PAYMENT("待支付", "等待买家支付"),
    PAID("已支付", "买家已完成支付"),
    SELLER_CONFIRMED("卖家已确认", "卖家已确认订单"),
    BUYER_CONFIRMED("买家已确认", "买家已确认收货"),
    COMPLETED("已完成", "订单已完成"),
    CANCELLED("已取消", "订单已取消"),
    TIMEOUT_CLOSED("超时关闭", "订单超时关闭");

    private final String name;
    private final String desc;

    OrderStatus(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static String getNameByCode(String code) {
        for (OrderStatus status : OrderStatus.values()) {
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
