package cse.jaejin.running.repository;

import cse.jaejin.running.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 사용자 ID로 게시글 목록을 조회하는 메서드
    List<Post> findByUserId(Long userId);

}