package com.cdwater.ticketbox.content.home.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Banner {

    public static final int STATUS_ENABLED = 1;

    private Long id;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
