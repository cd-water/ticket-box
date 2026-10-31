package com.cdwater.ticketbox.movie.application.query.view;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CrewView {

    private Long personId;
    private String name;
    private String avatar;
    private String roleName;
}
