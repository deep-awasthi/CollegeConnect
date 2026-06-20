package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.CircularRequest;
import com.collegeconnect.backend.model.Circular;
import com.collegeconnect.backend.repository.CircularRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class CircularService {

    @Autowired
    private CircularRepository circularRepository;

    @Transactional
    @CacheEvict(value = "circulars", allEntries = true)
    public Circular createCircular(CircularRequest request) {
        log.info("Creating a new circular and evicting circulars cache");
        Circular circular = Circular.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .postedBy(request.getPostedBy())
                .build();

        return circularRepository.save(circular);
    }

    @Cacheable(value = "circulars", key = "'all'")
    public List<Circular> getAllCirculars() {
        log.info("Fetching circulars from database (Cache Miss)");
        return circularRepository.findAllByOrderByCreatedAtDesc();
    }

    public Circular getCircularById(Long id) {
        return circularRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Circular not found with id: " + id));
    }
}
