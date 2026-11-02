package com.cdwater.ticketbox.order.api.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShowOrderDetailDTO extends ShowOrderListItemDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<ShowOrderSeatDTO> seats;
}