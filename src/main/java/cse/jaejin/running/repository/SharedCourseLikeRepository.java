package cse.jaejin.running.repository;

import cse.jaejin.running.domain.like.SharedCourseLike;
import cse.jaejin.running.domain.SharedCourse;
import cse.jaejin.running.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SharedCourseLikeRepository extends JpaRepository<SharedCourseLike, Long> {
    // 특정 사용자가 특정 SharedCourse에 좋아요를 눌렀는지 확인
    Optional<SharedCourseLike> findByUserAndSharedCourse(User user, SharedCourse sharedCourse);

    // 특정 SharedCourse의 좋아요 수 조회
    long countBySharedCourseId(Long sharedCourseId);
}