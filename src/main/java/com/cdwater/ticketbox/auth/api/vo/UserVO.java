package com.cdwater.ticketbox.auth.api.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String phone;

    private String nickname;

    private String avatar;
}
