package cse.jaejin.running.record;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningLocationPointService {

    private final RunningLocationPointRepository locationPointRepository;

    /**
     * 특정 러닝 기록 ID에 해당하는 위치 좌표 목록을 시간 순으로 조회
     */
    @Transactional(readOnly = true)
    public List<LocationPointDto> getPointsByRecordId(Long runningRecordId) {
        List<RunningLocationPoint> points = locationPointRepository.findByRunningRecordIdOrderByTimestampAsc(runningRecordId);

        return points.stream().map(p -> {
            LocationPointDto dto = new LocationPointDto();
            dto.setLatitude(p.getLatitude());
            dto.setLongitude(p.getLongitude());
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 특정 러닝 기록에 저장된 좌표 수 조회
     */
    @Transactional(readOnly = true)
    public int countPointsByRecordId(Long runningRecordId) {
        return locationPointRepository.findByRunningRecordId(runningRecordId).size();
    }

    /**
     * 특정 러닝 기록의 모든 좌표 삭제 (예: 기록 삭제 시 함께 사용)
     */
    @Transactional
    public void deletePointsByRecordId(Long runningRecordId) {
        List<RunningLocationPoint> points = locationPointRepository.findByRunningRecordId(runningRecordId);
        locationPointRepository.deleteAll(points);
    }
}
