package com.cdwater.movietimecinema.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SeatInfoVO {

    private Long screeningId;

    private Integer rowCount;

    private Integer colCount;

    List<SeatVO> lockSeats;
}

