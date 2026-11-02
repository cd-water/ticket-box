package com.cdwater.ticketbox.admin.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_admin")
public class AdminPO {

    public static final int STATUS_ENABLED = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    /** BCrypt 密文 */
    private String password;
    /** 0-禁用 1-启用 */
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
