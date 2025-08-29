package com.athletesedge.repository;

import com.athletesedge.model.PersonalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonalRecordRepository extends JpaRepository<PersonalRecord, Long> {
   
    List<PersonalRecord> findByAthleteIdOrderByRecordDateDesc(Long athleteId);
}