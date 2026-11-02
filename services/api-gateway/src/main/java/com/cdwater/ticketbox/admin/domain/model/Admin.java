package com.cdwater.ticketbox.admin.domain.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Admin {

    public static final int STATUS_ENABLED = 1;

    private Long id;

    private String username;

    /** BCrypt 密文 */
    private String password;

    /** 0-禁用 1-启用 */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    public boolean isEnabled() {
        return Integer.valueOf(STATUS_ENABLED).equals(status);
    }
}
