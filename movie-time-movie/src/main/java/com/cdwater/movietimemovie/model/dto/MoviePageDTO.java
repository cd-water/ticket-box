package com.cdwater.movietimemovie.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import lombok.Data;

@Data
public class MoviePageDTO extends PageRequest {

    private String name;

    private Integer status;
}
