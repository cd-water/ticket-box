package com.cdwater.movietimeuser.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import lombok.Data;

@Data
public class UserPageDTO extends PageRequest {

    private String phone;

    private String nickname;
}
