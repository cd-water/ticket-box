package com.cdwater.ticketbox.movie.adapter.out.persistence.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_movie_comment")
public class ReviewPO {

    public static final long ROOT_ID = 0L;
    public static final int STATUS_NORMAL = 1;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long movieId;
    private Long userId;
    private Long rootId;
    private Long parentId;
    private String content;
    private Integer likeCount;
    private Integer replyCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
