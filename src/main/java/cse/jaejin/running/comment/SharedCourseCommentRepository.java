package cse.jaejin.running.comment;

import cse.jaejin.running.sharedCourse.SharedCourse; // SharedCourse 엔티티 임포트
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SharedCourseCommentRepository extends JpaRepository<SharedCourseComment, Long> {
    /**
     * 특정 SharedCourse에 속한 모든 댓글을 조회합니다.
     * 댓글이 작성된 시간(createdAt)을 기준으로 오름차순 정렬합니다.
     */
    List<SharedCourseComment> findBySharedCourseOrderByCreatedAtAsc(SharedCourse sharedCourse);

    /**
     * 특정 SharedCourse의 ID를 통해 모든 댓글을 조회합니다.
     * 댓글이 작성된 시간(createdAt)을 기준으로 오름차순 정렬합니다.
     */
    List<SharedCourseComment> findBySharedCourseIdOrderByCreatedAtAsc(Long sharedCourseId);

    // 특정 SharedCourse의 댓글 수를 계산하는 메서드 (SharedCourse 엔티티의 commentCount 업데이트용)
    long countBySharedCourseId(Long sharedCourseId);
}