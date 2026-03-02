package com.cdwater.movietimemovie.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeMsg {

    private Long userId;

    private Long reviewId;

    private Long type;//操作类型：1点赞，2取消点赞
}
