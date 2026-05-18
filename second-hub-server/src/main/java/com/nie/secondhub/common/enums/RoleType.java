package com.nie.secondhub.common.enums;

public enum RoleType {
    USER("普通用户"),
    ADMIN("管理员");

    private final String description;

    RoleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static String getDescriptionByName(String name) {
        try {
            RoleType role = RoleType.valueOf(name.toUpperCase());
            return role.getDescription();
        } catch (IllegalArgumentException e) {
            return name;
        }
    }
}
