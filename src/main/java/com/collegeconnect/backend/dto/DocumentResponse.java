package com.collegeconnect.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DocumentResponse {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String description;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String department;
    private LocalDateTime createdAt;
}
