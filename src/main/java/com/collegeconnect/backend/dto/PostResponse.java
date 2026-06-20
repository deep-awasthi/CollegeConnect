package com.collegeconnect.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class PostResponse {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
}
