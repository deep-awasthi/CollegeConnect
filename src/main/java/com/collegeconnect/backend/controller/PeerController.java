package com.collegeconnect.backend.controller;

import com.collegeconnect.backend.dto.ConnectionResponse;
import com.collegeconnect.backend.dto.UserResponse;
import com.collegeconnect.backend.service.ConnectionService;
import com.collegeconnect.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/peers")
public class PeerController {

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private AuthService authService;

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getPeerProfile(@PathVariable Long id) {
        try {
            UserResponse response = authService.getUserProfileById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/connect/{peerId}")
    public ResponseEntity<?> sendConnectionRequest(@PathVariable Long peerId) {
        try {
            ConnectionResponse response = connectionService.sendConnectionRequest(peerId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept/{connectionId}")
    public ResponseEntity<?> acceptConnectionRequest(@PathVariable Long connectionId) {
        try {
            ConnectionResponse response = connectionService.acceptConnectionRequest(connectionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reject/{connectionId}")
    public ResponseEntity<?> rejectConnectionRequest(@PathVariable Long connectionId) {
        try {
            ConnectionResponse response = connectionService.rejectConnectionRequest(connectionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/connections")
    public ResponseEntity<List<ConnectionResponse>> getMyConnections() {
        return ResponseEntity.ok(connectionService.getMyConnections());
    }

    @GetMapping("/requests")
    public ResponseEntity<List<ConnectionResponse>> getMyPendingRequests() {
        return ResponseEntity.ok(connectionService.getPendingRequests());
    }
}
