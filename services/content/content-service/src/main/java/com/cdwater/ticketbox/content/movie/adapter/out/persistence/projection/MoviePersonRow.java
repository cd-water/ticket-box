package com.cdwater.ticketbox.content.movie.adapter.out.persistence.projection;

import lombok.Data;

@Data
public class MoviePersonRow {

    private Long movieId;
    private Long personId;
    private Integer duty;
    private String roleName;
    private Integer sortOrder;
    private String personName;
    private String personAvatar;
}
