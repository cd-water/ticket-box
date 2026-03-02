package com.cdwater.movietimecinema.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import lombok.Data;

@Data
public class CinemaPageDTO extends PageRequest {

    private String name;
}
