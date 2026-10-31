package com.cdwater.ticketbox.movie.infrastructure.persistence.projection;

import lombok.Data;

@Data
public class MovieCrewRow {

    private Long movieId;
    private Long personId;
    private Integer duty;
    private String roleName;
    private Integer sortOrder;
    private String personName;
    private String personAvatar;
}
