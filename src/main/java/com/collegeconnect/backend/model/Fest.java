package com.collegeconnect.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "fests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fest implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String location;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "coordinator_name")
    private String coordinatorName;

    @Column(name = "coordinator_email")
    private String coordinatorEmail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
