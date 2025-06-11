package cse.jaejin.running.course;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // 러닝 기록을 기반으로 코스 생성
    @PostMapping("/from-record")
    public ResponseEntity<Long> createCourseFromRunningRecord(
            @RequestBody CourseRequestDto request) {
        Long courseId = courseService.createCourseFromRunningRecord(request);
        return ResponseEntity.ok(courseId);
    }

    // 코스 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDto> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    // 전체 코스 조회
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // 사용자별 코스 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseResponseDto>> getCoursesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCoursesByUser(userId));
    }

    // 사용자 검색
    @GetMapping("/search")
    public ResponseEntity<List<CourseResponseDto>> searchByCourseTitle(@RequestParam String courseTitle) {
        return ResponseEntity.ok(courseService.searchByCourseTitle(courseTitle));
    }

}