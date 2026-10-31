package com.cdwater.ticketbox.movie.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResultVO {

    private boolean liked;
    private int likeCount;
}
