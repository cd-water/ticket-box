package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShowDetailDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String poster;
    private String description;
    private Integer duration;
    private String venueName;
    private String venueAddress;
    private LocalDateTime startTime;
    private List<TicketTypeDTO> ticketTypes;
}
