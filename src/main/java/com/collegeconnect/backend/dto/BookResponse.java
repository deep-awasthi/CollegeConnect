package com.collegeconnect.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookResponse {
    private Long id;
    private Long ownerId;
    private String ownerUsername;
    private String title;
    private String author;
    private String description;
    private Double price;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
}
