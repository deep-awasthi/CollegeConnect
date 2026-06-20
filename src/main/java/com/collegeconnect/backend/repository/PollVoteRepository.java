package com.collegeconnect.backend.repository;

import com.collegeconnect.backend.model.PollVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PollVoteRepository extends JpaRepository<PollVote, Long> {
    Boolean existsByPollIdAndUserId(Long pollId, Long userId);
    
    Optional<PollVote> findByPollIdAndUserId(Long pollId, Long userId);
    
    @Query("SELECT v.option.id, COUNT(v) FROM PollVote v WHERE v.poll.id = :pollId GROUP BY v.option.id")
    List<Object[]> countVotesByOptionForPoll(@Param("pollId") Long pollId);
}
