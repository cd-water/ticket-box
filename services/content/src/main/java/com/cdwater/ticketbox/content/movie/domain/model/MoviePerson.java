package com.cdwater.ticketbox.content.movie.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoviePerson {

    public static final int DUTY_DIRECTOR = 1;
    public static final int DUTY_ACTOR = 2;

    private Long personId;
    private String personName;
    private String personAvatar;
    private Integer duty;
    private String roleName;

    public boolean isDirector() {
        return Integer.valueOf(DUTY_DIRECTOR).equals(duty);
    }

    public boolean isActor() {
        return Integer.valueOf(DUTY_ACTOR).equals(duty);
    }
}
