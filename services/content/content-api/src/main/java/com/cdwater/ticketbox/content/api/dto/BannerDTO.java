package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class BannerDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String imageUrl;
    private String linkUrl;
}
