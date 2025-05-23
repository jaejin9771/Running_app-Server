package cse.jaejin.running.repository;

import cse.jaejin.running.domain.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningRecordRepository extends JpaRepository<RunningRecord, Long> {
    List<RunningRecord> findByUserId(Long userId);
}
