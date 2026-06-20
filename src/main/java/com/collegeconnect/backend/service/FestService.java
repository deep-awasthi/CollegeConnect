package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.FestRequest;
import com.collegeconnect.backend.model.Fest;
import com.collegeconnect.backend.repository.FestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class FestService {

    @Autowired
    private FestRepository festRepository;

    @Transactional
    @CacheEvict(value = "fests", allEntries = true)
    public Fest createFest(FestRequest request) {
        log.info("Creating a new fest and evicting fests cache");
        Fest fest = Fest.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .eventDate(request.getEventDate())
                .coordinatorName(request.getCoordinatorName())
                .coordinatorEmail(request.getCoordinatorEmail())
                .build();

        return festRepository.save(fest);
    }

    @Cacheable(value = "fests", key = "'all'")
    public List<Fest> getAllFests() {
        log.info("Fetching fests from database (Cache Miss)");
        return festRepository.findAllByOrderByEventDateAsc();
    }

    public Fest getFestById(Long id) {
        return festRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fest not found with id: " + id));
    }
}
