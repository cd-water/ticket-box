package com.cdwater.movietimeuser.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserPageVO {

    private String phone;

    private String nickname;

    private String avatar;

    private Integer gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String introduce;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
