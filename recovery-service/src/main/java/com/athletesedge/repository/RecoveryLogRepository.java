package com.athletesedge.repository;

import com.athletesedge.model.RecoveryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface RecoveryLogRepository extends JpaRepository<RecoveryLog, Long> {
    Optional<RecoveryLog> findByAthleteIdAndLogDate(Long athleteId, LocalDate date);
}