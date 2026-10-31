package com.cdwater.ticketbox.booking.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInfo {

    private Long id;
    private String title;
    private Integer status;
}
