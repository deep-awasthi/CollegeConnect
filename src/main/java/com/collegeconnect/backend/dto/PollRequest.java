package com.collegeconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PollRequest {
    @NotBlank
    private String question;

    @NotEmpty
    @Size(min = 2, max = 10, message = "Poll must have between 2 and 10 options")
    private List<String> options;

    private LocalDateTime expiresAt; // Optional expiration date
}
