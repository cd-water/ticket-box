package com.cdwater.ticketbox.movie.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResultView {

    private boolean liked;
    private int likeCount;
}
