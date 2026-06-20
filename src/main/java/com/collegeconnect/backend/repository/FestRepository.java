package com.collegeconnect.backend.repository;

import com.collegeconnect.backend.model.Fest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FestRepository extends JpaRepository<Fest, Long> {
    List<Fest> findAllByOrderByEventDateAsc();
}
