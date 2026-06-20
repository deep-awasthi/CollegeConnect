package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.PollOptionDto;
import com.collegeconnect.backend.dto.PollRequest;
import com.collegeconnect.backend.dto.PollResponse;
import com.collegeconnect.backend.dto.VoteRequest;
import com.collegeconnect.backend.model.Poll;
import com.collegeconnect.backend.model.PollOption;
import com.collegeconnect.backend.model.PollVote;
import com.collegeconnect.backend.model.User;
import com.collegeconnect.backend.repository.PollRepository;
import com.collegeconnect.backend.repository.PollVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private PollVoteRepository pollVoteRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public PollResponse createPoll(PollRequest request) {
        User currentUser = authService.getCurrentUserEntity();

        Poll poll = Poll.builder()
                .creator(currentUser)
                .question(request.getQuestion())
                .expiresAt(request.getExpiresAt())
                .build();

        List<PollOption> options = request.getOptions().stream()
                .map(optText -> PollOption.builder()
                        .poll(poll)
                        .optionText(optText)
                        .build())
                .collect(Collectors.toList());

        poll.setOptions(options);
        Poll saved = pollRepository.save(poll);
        return mapToPollResponse(saved, currentUser.getId());
    }

    public List<PollResponse> getAllPolls() {
        User currentUser = authService.getCurrentUserEntity();
        List<Poll> polls = pollRepository.findAllByOrderByCreatedAtDesc();
        return polls.stream()
                .map(p -> mapToPollResponse(p, currentUser.getId()))
                .collect(Collectors.toList());
    }

    public PollResponse getPollById(Long id) {
        User currentUser = authService.getCurrentUserEntity();
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found with id: " + id));
        return mapToPollResponse(poll, currentUser.getId());
    }

    @Transactional
    public PollResponse voteOnPoll(Long pollId, VoteRequest request) {
        User currentUser = authService.getCurrentUserEntity();
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new IllegalArgumentException("Poll not found with id: " + pollId));

        // Check if poll has expired
        if (poll.getExpiresAt() != null && LocalDateTime.now().isAfter(poll.getExpiresAt())) {
            throw new IllegalArgumentException("This poll has expired!");
        }

        // Check if user has already voted
        if (pollVoteRepository.existsByPollIdAndUserId(pollId, currentUser.getId())) {
            throw new IllegalArgumentException("You have already voted in this poll!");
        }

        // Check if option belongs to this poll
        PollOption selectedOption = poll.getOptions().stream()
                .filter(opt -> opt.getId().equals(request.getOptionId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option not found or does not belong to this poll!"));

        PollVote vote = PollVote.builder()
                .poll(poll)
                .option(selectedOption)
                .user(currentUser)
                .build();

        pollVoteRepository.save(vote);

        return mapToPollResponse(poll, currentUser.getId());
    }

    private PollResponse mapToPollResponse(Poll p, Long currentUserId) {
        // Fetch votes count for each option in this poll
        List<Object[]> countResults = pollVoteRepository.countVotesByOptionForPoll(p.getId());
        Map<Long, Long> optionVotesMap = countResults.stream()
                .collect(Collectors.toMap(
                        res -> (Long) res[0],
                        res -> (Long) res[1]
                ));

        // Map options to PollOptionDto
        List<PollOptionDto> optionDtos = p.getOptions().stream()
                .map(opt -> PollOptionDto.builder()
                        .id(opt.getId())
                        .optionText(opt.getOptionText())
                        .voteCount(optionVotesMap.getOrDefault(opt.getId(), 0L))
                        .build())
                .collect(Collectors.toList());

        long totalVotes = optionDtos.stream().mapToLong(PollOptionDto::getVoteCount).sum();

        // Check if current user has voted on this poll
        Optional<PollVote> userVoteOpt = pollVoteRepository.findByPollIdAndUserId(p.getId(), currentUserId);
        boolean hasVoted = userVoteOpt.isPresent();
        Long selectedOptionId = hasVoted ? userVoteOpt.get().getOption().getId() : null;

        return PollResponse.builder()
                .id(p.getId())
                .creatorId(p.getCreator().getId())
                .creatorUsername(p.getCreator().getUsername())
                .question(p.getQuestion())
                .options(optionDtos)
                .expiresAt(p.getExpiresAt())
                .createdAt(p.getCreatedAt())
                .hasVoted(hasVoted)
                .selectedOptionId(selectedOptionId)
                .totalVotes(totalVotes)
                .build();
    }
}
