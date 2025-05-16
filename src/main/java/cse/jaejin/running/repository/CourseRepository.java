package cse.jaejin.running.repository;

import cse.jaejin.running.domain.Course;
import cse.jaejin.running.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUser(User user);
    List<Course> findByCourseTitleContainingIgnoreCase(String courseTitle); // 이름에 부분 일치, 대소문자 무시
}
