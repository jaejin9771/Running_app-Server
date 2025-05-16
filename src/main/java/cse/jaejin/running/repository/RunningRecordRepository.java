package cse.jaejin.running.repository;

import cse.jaejin.running.domain.RunningRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RunningRecordRepository extends JpaRepository<RunningRecord, Long> {
}
