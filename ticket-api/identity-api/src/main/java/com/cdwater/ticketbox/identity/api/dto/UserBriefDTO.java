package com.cdwater.ticketbox.identity.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBriefDTO implements Serializable {

    private Long id;
    private String nickname;
    private String avatar;
}
