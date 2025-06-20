package cse.jaejin.running.course;

import cse.jaejin.running.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByUser(User user);
    List<Course> findByCourseTitleContainingIgnoreCase(String courseTitle); // 이름에 부분 일치, 대소문자 무시
}
