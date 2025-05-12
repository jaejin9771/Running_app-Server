package cse.jaejin.running.controller;

import cse.jaejin.running.converter.CourseConverter;
import cse.jaejin.running.domain.Course;
import cse.jaejin.running.domain.LocationPoint;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.CourseDetailDto;
import cse.jaejin.running.dto.CourseDto;
import cse.jaejin.running.dto.CourseSummaryDto;
import cse.jaejin.running.repository.CourseRepository;
import cse.jaejin.running.repository.UserRepository;
import cse.jaejin.running.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    // 코스 등록
    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody CourseDto dto) {
        Optional<Long> result = courseService.saveCourse(dto);

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

    // 전체 코스 조회
    @GetMapping
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // 특정 코스 조회
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailDto> getCourseDetail(@PathVariable Long id) {
        return courseRepository.findById(id)
                .map(CourseConverter::toDetailDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // 특정 유저 코스 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CourseSummaryDto>> getCoursesByUser(@PathVariable Long userId) {
        List<Course> courses = courseRepository.findByUserId(userId);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<CourseSummaryDto> dtos = courses.stream()
                .map(CourseConverter::toSummaryDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }


}

