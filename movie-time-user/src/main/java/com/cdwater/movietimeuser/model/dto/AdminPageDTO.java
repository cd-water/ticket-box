package com.cdwater.movietimeuser.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import lombok.Data;

@Data
public class AdminPageDTO extends PageRequest {

    private String username;
}
