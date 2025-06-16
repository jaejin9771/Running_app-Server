package cse.jaejin.running.record;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/running-records")
@RequiredArgsConstructor
public class RunningRecordController {

    private final RunningRecordService runningRecordService;

    // 러닝 기록 등록
    @PostMapping
    public ResponseEntity<Long> createRunningRecord(@RequestBody RunningRecordRequestDto requestDto) {
        Long id = runningRecordService.createRunningRecord(requestDto);
        return ResponseEntity.ok(id);
    }

    // 특정 사용자에 대한 러닝 기록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RunningRecordResponseDto>> getRecordsByUser(@PathVariable Long userId) {
        List<RunningRecordResponseDto> records = runningRecordService.getRecordsByUser(userId);
        return ResponseEntity.ok(records);
    }

    // 개별 러닝 기록 조회
    @GetMapping("/{id}")
    public ResponseEntity<RunningRecordResponseDto> getRunningRecord(@PathVariable Long id) {
        RunningRecordResponseDto record = runningRecordService.getRunningRecord(id);
        return ResponseEntity.ok(record);
    }

    // 러닝 기록 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRunningRecord(@PathVariable Long id) {
        runningRecordService.deleteRunningRecord(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/title")
    public ResponseEntity<String> updateRunningRecordTitle(
            @PathVariable Long id,
            @RequestBody UpdateRunningRecordTitleRequestDto requestDto) {
        runningRecordService.updateRunningRecordTitle(id, requestDto.getTitle());
        return ResponseEntity.ok("러닝 기록 제목이 성공적으로 수정되었습니다.");
    }
}
