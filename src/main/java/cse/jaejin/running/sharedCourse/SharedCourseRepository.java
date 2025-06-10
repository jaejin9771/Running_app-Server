package cse.jaejin.running.sharedCourse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SharedCourseRepository extends JpaRepository<SharedCourse, Long> {
    List<SharedCourse> findByCategory(String category);
    List<SharedCourse> findByUserId(Long userId);
}
