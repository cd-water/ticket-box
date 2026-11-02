package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long personId;
    private String name;
    private String avatar;
    private String roleName;
}
