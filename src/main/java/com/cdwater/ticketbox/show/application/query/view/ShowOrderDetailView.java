package com.cdwater.ticketbox.show.application.query.view;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShowOrderDetailView extends ShowOrderListItemView {

    private List<ShowOrderSeatView> seats;
}
