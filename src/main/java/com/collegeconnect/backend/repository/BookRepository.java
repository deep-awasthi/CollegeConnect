package com.collegeconnect.backend.repository;

import com.collegeconnect.backend.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByIsAvailableTrueOrderByCreatedAtDesc();
    List<Book> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
}
