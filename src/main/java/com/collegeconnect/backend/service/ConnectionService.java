package com.collegeconnect.backend.service;

import com.collegeconnect.backend.dto.ConnectionResponse;
import com.collegeconnect.backend.model.Connection;
import com.collegeconnect.backend.model.ConnectionStatus;
import com.collegeconnect.backend.model.User;
import com.collegeconnect.backend.repository.ConnectionRepository;
import com.collegeconnect.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public ConnectionResponse sendConnectionRequest(Long peerId) {
        User currentUser = authService.getCurrentUserEntity();
        if (currentUser.getId().equals(peerId)) {
            throw new IllegalArgumentException("You cannot connect with yourself!");
        }

        User peer = userRepository.findById(peerId)
                .orElseThrow(() -> new IllegalArgumentException("Peer not found with id: " + peerId));

        Optional<Connection> existingOpt = connectionRepository.findConnectionBetween(currentUser.getId(), peerId);
        if (existingOpt.isPresent()) {
            throw new IllegalArgumentException("Connection or request already exists between you and this peer!");
        }

        Connection connection = Connection.builder()
                .user(currentUser)
                .peer(peer)
                .status(ConnectionStatus.PENDING)
                .build();

        Connection saved = connectionRepository.save(connection);
        return mapToConnectionResponse(saved);
    }

    @Transactional
    public ConnectionResponse acceptConnectionRequest(Long connectionId) {
        User currentUser = authService.getCurrentUserEntity();
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Connection request not found with id: " + connectionId));

        if (!connection.getPeer().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to accept this connection request!");
        }

        if (connection.getStatus() != ConnectionStatus.PENDING) {
            throw new IllegalArgumentException("Connection request is not in PENDING state!");
        }

        connection.setStatus(ConnectionStatus.ACCEPTED);
        Connection saved = connectionRepository.save(connection);
        return mapToConnectionResponse(saved);
    }

    @Transactional
    public ConnectionResponse rejectConnectionRequest(Long connectionId) {
        User currentUser = authService.getCurrentUserEntity();
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("Connection request not found with id: " + connectionId));

        if (!connection.getPeer().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("You are not authorized to reject this connection request!");
        }

        if (connection.getStatus() != ConnectionStatus.PENDING) {
            throw new IllegalArgumentException("Connection request is not in PENDING state!");
        }

        connection.setStatus(ConnectionStatus.REJECTED);
        Connection saved = connectionRepository.save(connection);
        return mapToConnectionResponse(saved);
    }

    public List<ConnectionResponse> getMyConnections() {
        User currentUser = authService.getCurrentUserEntity();
        List<Connection> connections = connectionRepository.findConnectionsByUserIdAndStatus(currentUser.getId(), ConnectionStatus.ACCEPTED);
        return connections.stream()
                .map(this::mapToConnectionResponse)
                .collect(Collectors.toList());
    }

    public List<ConnectionResponse> getPendingRequests() {
        User currentUser = authService.getCurrentUserEntity();
        // Return requests sent TO the current user that are PENDING
        List<Connection> connections = connectionRepository.findConnectionsByUserIdAndStatus(currentUser.getId(), ConnectionStatus.PENDING);
        return connections.stream()
                .filter(c -> c.getPeer().getId().equals(currentUser.getId())) // Filter where currentUser is the target/receiver
                .map(this::mapToConnectionResponse)
                .collect(Collectors.toList());
    }

    private ConnectionResponse mapToConnectionResponse(Connection c) {
        return ConnectionResponse.builder()
                .id(c.getId())
                .requesterId(c.getUser().getId())
                .requesterUsername(c.getUser().getUsername())
                .peerId(c.getPeer().getId())
                .peerUsername(c.getPeer().getUsername())
                .status(c.getStatus().name())
                .build();
    }
}
