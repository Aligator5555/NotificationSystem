package org.example.notificationsystem3728.repository;

import org.example.notificationsystem3728.model.Signal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignalRepository extends JpaRepository<Signal, Long> {
    Optional<Signal> findByFilePath(String filePath);

}
