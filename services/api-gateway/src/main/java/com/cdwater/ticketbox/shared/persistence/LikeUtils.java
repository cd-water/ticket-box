package com.cdwater.ticketbox.shared.persistence;

public final class LikeUtils {

    private LikeUtils() {
    }

    public static String escape(String value) {
        return value.replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }
}
