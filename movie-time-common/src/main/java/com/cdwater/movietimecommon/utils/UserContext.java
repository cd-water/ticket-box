package com.cdwater.movietimecommon.utils;

/**
 * 用户上下文
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    private static final ThreadLocal<String> USER_PHONE = new ThreadLocal<>();

    public static void setCurrentUser(Long userId, String phone) {
        USER_ID.set(userId);
        USER_PHONE.set(phone);
    }

    public static Long getId() {
        return USER_ID.get();
    }

    public static String getPhone() {
        return USER_PHONE.get();
    }

    public static void removeCurrentUser() {
        USER_ID.remove();
        USER_PHONE.remove();
    }
}
