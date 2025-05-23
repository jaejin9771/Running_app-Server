package cse.jaejin.running.repository;

import cse.jaejin.running.domain.Comment;
import cse.jaejin.running.domain.SharedCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySharedCourseIdOrderByCreatedAtDesc(Long sharedCourseId);
}
