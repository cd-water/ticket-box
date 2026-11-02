package com.cdwater.ticketbox.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long total;
    private List<T> records;
    private long page;
    private long size;

    public static <T> PageResult<T> of(long total, List<T> records, long page, long size) {
        return new PageResult<>(total, records, page, size);
    }
}
