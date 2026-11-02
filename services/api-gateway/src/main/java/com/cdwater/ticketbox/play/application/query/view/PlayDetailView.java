package com.cdwater.ticketbox.play.application.query.view;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PlayDetailView extends PlayListItemView {

    private String description;
}
