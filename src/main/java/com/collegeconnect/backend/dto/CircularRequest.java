package com.collegeconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CircularRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private String postedBy;
}
