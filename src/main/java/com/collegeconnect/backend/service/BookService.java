package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.BookRequest;
import com.collegeconnect.backend.dto.BookResponse;
import com.collegeconnect.backend.model.Book;
import com.collegeconnect.backend.model.User;
import com.collegeconnect.backend.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public BookResponse createBookListing(BookRequest request) {
        User currentUser = authService.getCurrentUserEntity();
        Book book = Book.builder()
                .owner(currentUser)
                .title(request.getTitle())
                .author(request.getAuthor())
                .description(request.getDescription())
                .price(request.getPrice())
                .isAvailable(true)
                .build();

        Book saved = bookRepository.save(book);
        return mapToBookResponse(saved);
    }

    public List<BookResponse> getAvailableBooks() {
        List<Book> books = bookRepository.findByIsAvailableTrueOrderByCreatedAtDesc();
        return books.stream().map(this::mapToBookResponse).collect(Collectors.toList());
    }

    public List<BookResponse> getMyBookListings() {
        User currentUser = authService.getCurrentUserEntity();
        List<Book> books = bookRepository.findByOwnerIdOrderByCreatedAtDesc(currentUser.getId());
        return books.stream().map(this::mapToBookResponse).collect(Collectors.toList());
    }

    @Transactional
    public BookResponse claimBook(Long bookId) {
        User currentUser = authService.getCurrentUserEntity();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found with id: " + bookId));

        if (!book.getIsAvailable()) {
            throw new IllegalArgumentException("Book is already claimed or unavailable!");
        }

        if (book.getOwner().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You cannot claim your own book listing!");
        }

        book.setIsAvailable(false);
        Book saved = bookRepository.save(book);
        return mapToBookResponse(saved);
    }

    private BookResponse mapToBookResponse(Book b) {
        return BookResponse.builder()
                .id(b.getId())
                .ownerId(b.getOwner().getId())
                .ownerUsername(b.getOwner().getUsername())
                .title(b.getTitle())
                .author(b.getAuthor())
                .description(b.getDescription())
                .price(b.getPrice())
                .isAvailable(b.getIsAvailable())
                .createdAt(b.getCreatedAt())
                .build();
    }
}
