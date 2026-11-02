package com.cdwater.ticketbox.content.home.application.port;

import com.cdwater.ticketbox.content.api.dto.HomeShowDTO;

import java.util.List;

public interface HomeShowPort {

    List<HomeShowDTO> findUpcoming(int limit);
}