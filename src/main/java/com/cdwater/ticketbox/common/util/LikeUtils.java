package com.cdwater.ticketbox.common.util;

public final class LikeUtils {

    private LikeUtils() {
    }

    public static String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
