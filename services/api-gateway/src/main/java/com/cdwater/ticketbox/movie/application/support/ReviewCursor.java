package com.cdwater.ticketbox.movie.application.support;

import com.cdwater.ticketbox.shared.error.BizException;
import com.cdwater.ticketbox.shared.error.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Base64;

public final class ReviewCursor {

    private static final String SEP = "|";

    private ReviewCursor() {
    }

    public static String encode(String value, long id) {
        String raw = value + SEP + id;
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static Cursor decode(String cursor) {
        try {
            String raw = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
            int index = raw.lastIndexOf(SEP);
            if (index <= 0) {
                throw new IllegalArgumentException();
            }
            return new Cursor(raw.substring(0, index), Long.parseLong(raw.substring(index + 1)));
        } catch (Exception e) {
            throw invalid();
        }
    }

    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw invalid();
        }
    }

    public static LocalDateTime parseTime(String value) {
        try {
            LocalDateTime time = LocalDateTime.parse(value);
            if (time.getYear() < 1000 || time.getYear() > 9999) {
                throw invalid();
            }
            return time;
        } catch (DateTimeParseException e) {
            throw invalid();
        }
    }

    private static BizException invalid() {
        return new BizException(ResultCode.BAD_REQUEST, "无效的分页游标");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Cursor {

        private String value;
        private long id;
    }
}
