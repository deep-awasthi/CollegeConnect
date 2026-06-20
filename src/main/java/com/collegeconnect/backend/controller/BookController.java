package com.collegeconnect.backend.controller;

import com.collegeconnect.backend.dto.BookRequest;
import com.collegeconnect.backend.dto.BookResponse;
import com.collegeconnect.backend.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<?> createBookListing(@Valid @RequestBody BookRequest request) {
        try {
            BookResponse response = bookService.createBookListing(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @GetMapping("/my")
    public ResponseEntity<List<BookResponse>> getMyBookListings() {
        return ResponseEntity.ok(bookService.getMyBookListings());
    }

    @PostMapping("/{id}/claim")
    public ResponseEntity<?> claimBook(@PathVariable Long id) {
        try {
            BookResponse response = bookService.claimBook(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
