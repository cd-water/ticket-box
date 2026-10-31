package com.cdwater.ticketbox.movie.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewUserVO {

    private Long id;
    private String nickname;
    private String avatar;
}
