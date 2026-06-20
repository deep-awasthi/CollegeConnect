package com.collegeconnect.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PollResponse {
    private Long id;
    private Long creatorId;
    private String creatorUsername;
    private String question;
    private List<PollOptionDto> options;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private Boolean hasVoted;
    private Long selectedOptionId;
    private Long totalVotes;
}
