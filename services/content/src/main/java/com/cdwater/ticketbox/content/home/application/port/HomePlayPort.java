package com.cdwater.ticketbox.content.home.application.port;

import com.cdwater.ticketbox.content.api.dto.HomePlayDTO;

import java.util.List;

public interface HomePlayPort {

    List<HomePlayDTO> findOnSale(int limit);
}