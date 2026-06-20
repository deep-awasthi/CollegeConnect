package com.collegeconnect.backend.repository;

import com.collegeconnect.backend.model.Circular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CircularRepository extends JpaRepository<Circular, Long> {
    List<Circular> findAllByOrderByCreatedAtDesc();
}
