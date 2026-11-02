package com.cdwater.ticketbox.home.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_banner")
public class BannerPO {

    public static final int STATUS_ENABLED = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String imageUrl;
    private String linkUrl;
    private Integer sortOrder;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
