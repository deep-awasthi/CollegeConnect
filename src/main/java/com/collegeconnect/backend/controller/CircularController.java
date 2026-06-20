package com.collegeconnect.backend.controller;

import com.collegeconnect.backend.dto.CircularRequest;
import com.collegeconnect.backend.model.Circular;
import com.collegeconnect.backend.service.CircularService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/circulars")
public class CircularController {

    @Autowired
    private CircularService circularService;

    @PostMapping
    public ResponseEntity<?> createCircular(@Valid @RequestBody CircularRequest request) {
        try {
            Circular response = circularService.createCircular(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Circular>> getAllCirculars() {
        return ResponseEntity.ok(circularService.getAllCirculars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCircularById(@PathVariable Long id) {
        try {
            Circular response = circularService.getCircularById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
