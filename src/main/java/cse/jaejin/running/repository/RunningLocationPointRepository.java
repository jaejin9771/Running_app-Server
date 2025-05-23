package cse.jaejin.running.repository;

import cse.jaejin.running.domain.RunningLocationPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunningLocationPointRepository extends JpaRepository<RunningLocationPoint, Long> {

    // 특정 러닝 기록에 해당하는 위치 좌표들을 시간 순으로 가져오기
    List<RunningLocationPoint> findByRunningRecordIdOrderByTimestampAsc(Long runningRecordId);

    // 특정 러닝 기록에 해당하는 모든 좌표 가져오기
    List<RunningLocationPoint> findByRunningRecordId(Long runningRecordId);
}
