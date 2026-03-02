package com.cdwater.movietimecommon;

import lombok.Data;

@Data
public class PageRequest {

    private int pageNum = 1;

    private int pageSize = 10;
}
