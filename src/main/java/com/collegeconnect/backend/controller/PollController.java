package com.collegeconnect.backend.controller;

import com.collegeconnect.backend.dto.PollRequest;
import com.collegeconnect.backend.dto.PollResponse;
import com.collegeconnect.backend.dto.VoteRequest;
import com.collegeconnect.backend.service.PollService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollService pollService;

    @PostMapping
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest request) {
        try {
            PollResponse response = pollService.createPoll(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PollResponse>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPollById(@PathVariable Long id) {
        try {
            PollResponse response = pollService.getPollById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/vote")
    public ResponseEntity<?> voteOnPoll(@PathVariable Long id, @Valid @RequestBody VoteRequest request) {
        try {
            PollResponse response = pollService.voteOnPoll(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
