package com.cdwater.ticketbox.home.application.port;

import com.cdwater.ticketbox.home.application.query.view.HomeShowItem;

import java.util.List;

public interface HomeShowPort {

    List<HomeShowItem> findUpcoming(int limit);
}
