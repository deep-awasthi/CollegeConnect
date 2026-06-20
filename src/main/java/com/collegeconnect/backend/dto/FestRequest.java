package com.collegeconnect.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FestRequest {
    @NotBlank
    private String title;

    private String description;
    private String location;
    private LocalDateTime eventDate;
    private String coordinatorName;

    @Email
    private String coordinatorEmail;
}
