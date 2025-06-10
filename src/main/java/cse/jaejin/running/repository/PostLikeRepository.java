package cse.jaejin.running.repository;

import cse.jaejin.running.domain.like.PostLike;
import cse.jaejin.running.domain.Post;
import cse.jaejin.running.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 특정 사용자가 특정 Post에 좋아요를 눌렀는지 확인
    Optional<PostLike> findByUserAndPost(User user, Post post);

    // 특정 Post의 좋아요 수 조회
    long countByPostId(Long postId);
}