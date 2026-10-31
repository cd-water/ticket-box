package com.cdwater.ticketbox.movie.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewUserView {

    private Long id;
    private String nickname;
    private String avatar;
}
