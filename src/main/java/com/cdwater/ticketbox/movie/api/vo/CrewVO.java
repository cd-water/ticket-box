package com.cdwater.ticketbox.movie.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewVO {

    private Long personId;
    private String name;
    private String avatar;
    private String roleName;
}
