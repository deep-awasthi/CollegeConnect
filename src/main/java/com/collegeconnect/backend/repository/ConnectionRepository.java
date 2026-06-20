package com.collegeconnect.backend.repository;

import com.collegeconnect.backend.model.Connection;
import com.collegeconnect.backend.model.ConnectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
    @Query("SELECT c FROM Connection c WHERE (c.user.id = :userId OR c.peer.id = :userId) AND c.status = :status")
    List<Connection> findConnectionsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") ConnectionStatus status);

    @Query("SELECT c FROM Connection c WHERE (c.user.id = :userId1 AND c.peer.id = :userId2) OR (c.user.id = :userId2 AND c.peer.id = :userId1)")
    Optional<Connection> findConnectionBetween(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
