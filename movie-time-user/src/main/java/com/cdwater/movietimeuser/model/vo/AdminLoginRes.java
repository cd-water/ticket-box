package com.cdwater.movietimeuser.model.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginRes {

    private Long id;

    private String username;

    private Integer top;

    private String token;
}
