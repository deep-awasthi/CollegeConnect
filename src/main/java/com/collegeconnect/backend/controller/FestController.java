package com.collegeconnect.backend.controller;

import com.collegeconnect.backend.dto.FestRequest;
import com.collegeconnect.backend.model.Fest;
import com.collegeconnect.backend.service.FestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fests")
public class FestController {

    @Autowired
    private FestService festService;

    @PostMapping
    public ResponseEntity<?> createFest(@Valid @RequestBody FestRequest request) {
        try {
            Fest response = festService.createFest(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Fest>> getAllFests() {
        return ResponseEntity.ok(festService.getAllFests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFestById(@PathVariable Long id) {
        try {
            Fest response = festService.getFestById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
