package com.cdwater.movietimeorder.model.dto;

import com.cdwater.movietimecommon.PageRequest;
import lombok.Data;

@Data
public class OrderPageDTO extends PageRequest {

    private Long orderNo;

    private Integer status;
}
