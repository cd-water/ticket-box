package com.cdwater.movietimeuser.mq.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowMsg {

    private Long followerId;

    private Long followeeId;

    private Long type;//操作类型：1-关注 0-取关
}
