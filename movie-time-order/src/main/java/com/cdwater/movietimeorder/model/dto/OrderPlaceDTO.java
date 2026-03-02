package com.cdwater.movietimeorder.model.dto;

import com.cdwater.movietimecinema.model.vo.SeatVO;
import lombok.Data;

import java.util.List;

@Data
public class OrderPlaceDTO {

    private Long screeningId;

    private List<SeatVO> placeSeats;
}
