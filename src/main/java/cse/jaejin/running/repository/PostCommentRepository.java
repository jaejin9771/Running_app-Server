package cse.jaejin.running.repository;

import cse.jaejin.running.domain.PostComment;
import cse.jaejin.running.domain.Post; // Post 엔티티 임포트
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    /**
     * 특정 Post에 속한 모든 댓글을 조회합니다.
     * 댓글이 작성된 시간(createdAt)을 기준으로 오름차순 정렬합니다.
     */
    List<PostComment> findByPostOrderByCreatedAtAsc(Post post);

    /**
     * 특정 Post의 ID를 통해 모든 댓글을 조회합니다.
     * 댓글이 작성된 시간(createdAt)을 기준으로 오름차순 정렬합니다.
     */
    List<PostComment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // 특정 Post의 댓글 수를 계산하는 메서드 (Post 엔티티의 commentCount 업데이트용)
    long countByPostId(Long postId);
}