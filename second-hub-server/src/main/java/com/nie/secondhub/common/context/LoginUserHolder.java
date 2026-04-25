package com.nie.secondhub.common.context;

public final class LoginUserHolder {
    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    private LoginUserHolder() {
    }

    public static void set(LoginUser loginUser) {
        HOLDER.set(loginUser);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static Long requireUserId() {
        LoginUser loginUser = HOLDER.get();
        if (loginUser == null) {
            throw new RuntimeException("未登录或令牌缺失");
        }
        return loginUser.getUserId();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
