package com.cdwater.ticketbox.user.application.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBrief {

    private Long id;
    private String nickname;
    private String avatar;
}
