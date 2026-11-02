package com.cdwater.ticketbox.content.api.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewCommandDTO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(max = 1000)
    private String content;
}
