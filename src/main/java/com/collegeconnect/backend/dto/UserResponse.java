package com.collegeconnect.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String college;
    private String department;
    private Integer graduationYear;
    private String bio;
}
