package com.collegeconnect.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionResponse {
    private Long id;
    private Long requesterId;
    private String requesterUsername;
    private Long peerId;
    private String peerUsername;
    private String status;
}
