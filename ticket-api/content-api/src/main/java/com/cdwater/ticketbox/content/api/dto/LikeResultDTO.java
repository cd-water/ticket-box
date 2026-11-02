package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResultDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean liked;
    private int likeCount;
}
