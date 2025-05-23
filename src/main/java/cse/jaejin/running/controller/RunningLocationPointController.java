package cse.jaejin.running.controller;

import cse.jaejin.running.dto.LocationPointDto;
import cse.jaejin.running.service.RunningLocationPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location-points")
@RequiredArgsConstructor
public class RunningLocationPointController {

    private final RunningLocationPointService locationPointService;

    /**
     * 러닝 기록 ID로 GPS 위치 리스트 조회
     * 예: GET /api/location-points?recordId=12
     */
    @GetMapping
    public ResponseEntity<List<LocationPointDto>> getPointsByRecordId(@RequestParam("recordId") Long recordId) {
        List<LocationPointDto> points = locationPointService.getPointsByRecordId(recordId);
        return ResponseEntity.ok(points);
    }

    /**
     * 좌표 개수 반환 (선택사항, 통계용)
     * 예: GET /api/location-points/count?recordId=12
     */
    @GetMapping("/count")
    public ResponseEntity<Integer> countPoints(@RequestParam("recordId") Long recordId) {
        int count = locationPointService.countPointsByRecordId(recordId);
        return ResponseEntity.ok(count);
    }

    /**
     * 러닝 기록 ID로 위치 좌표 전체 삭제
     * 예: DELETE /api/location-points?recordId=12
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePointsByRecordId(@RequestParam("recordId") Long recordId) {
        locationPointService.deletePointsByRecordId(recordId);
        return ResponseEntity.noContent().build();
    }
}
