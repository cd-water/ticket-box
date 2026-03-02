package com.cdwater.movietimecommon.utils;

/**
 * 管理员上下文
 */
public class AdminContext {

    private static final ThreadLocal<Long> ADMIN_ID = new ThreadLocal<>();

    private static final ThreadLocal<Integer> ADMIN_PERMISSION = new ThreadLocal<>();

    public static void setCurrentAdmin(Long adminId, Integer adminPermission) {
        ADMIN_ID.set(adminId);
        ADMIN_PERMISSION.set(adminPermission);
    }

    public static Long getId() {
        return ADMIN_ID.get();
    }

    public static Integer getPermission() {
        return ADMIN_PERMISSION.get();
    }

    public static void removeCurrentAdmin() {
        ADMIN_ID.remove();
        ADMIN_PERMISSION.remove();
    }
}
