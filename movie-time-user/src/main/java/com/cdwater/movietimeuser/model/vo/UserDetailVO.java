package com.cdwater.movietimeuser.model.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserDetailVO {

    private Long id;

    private String nickname;

    private String avatar;

    private Integer gender;

    private LocalDate birthday;

    private String introduce;

    private Integer fanCount;

    private Integer followerCount;

    private Boolean hasFollowed;
}