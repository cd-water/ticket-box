package com.cdwater.ticketbox.home.application.port;

import com.cdwater.ticketbox.home.application.query.view.HomePlayItem;

import java.util.List;

public interface HomePlayPort {

    List<HomePlayItem> findOnSale(int limit);
}
