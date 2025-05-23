package cse.jaejin.running.controller;

import cse.jaejin.running.dto.SharedCourseRequestDto;
import cse.jaejin.running.dto.SharedCourseResponseDto;
import cse.jaejin.running.service.SharedCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shared-courses")
@RequiredArgsConstructor
public class SharedCourseController {

    private final SharedCourseService sharedCourseService;

    @GetMapping("/{id}")
    public ResponseEntity<SharedCourseResponseDto> getSharedCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(sharedCourseService.getSharedCourseById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSharedCourse(@PathVariable Long id) {
        sharedCourseService.deleteSharedCourse(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Long> createSharedCourse(@RequestBody SharedCourseRequestDto dto) {
        return ResponseEntity.ok(sharedCourseService.createSharedCourse(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSharedCourse(
            @PathVariable Long id,
            @RequestBody SharedCourseRequestDto requestDto) {
        sharedCourseService.updateSharedCourse(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SharedCourseResponseDto>> getAllSharedCourses() {
        return ResponseEntity.ok(sharedCourseService.getAllSharedCourses());
    }

    @GetMapping("/category")
    public ResponseEntity<List<SharedCourseResponseDto>> getByCategory(@RequestParam String category) {
        return ResponseEntity.ok(sharedCourseService.getSharedCoursesByCategory(category));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SharedCourseResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(sharedCourseService.getSharedCoursesByUserId(userId));
    }

}

