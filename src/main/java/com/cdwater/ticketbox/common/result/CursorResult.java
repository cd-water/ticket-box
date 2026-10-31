package com.cdwater.ticketbox.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CursorResult<T> {

    private List<T> records;
    private String nextCursor;
    private boolean hasMore;

    public static <T> CursorResult<T> of(List<T> records, String nextCursor, boolean hasMore) {
        return new CursorResult<>(records, nextCursor, hasMore);
    }
}
