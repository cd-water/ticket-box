package com.cdwater.ticketbox.show.application.query.view;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShowDetailView {

    private Long id;
    private String title;
    private String poster;
    private String description;
    private Integer duration;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private List<TicketTypeView> ticketTypes;
}
