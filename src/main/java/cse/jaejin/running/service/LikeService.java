package cse.jaejin.running.service;

import cse.jaejin.running.domain.Post;
import cse.jaejin.running.domain.SharedCourse;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.domain.like.PostLike; // PostLike 엔티티
import cse.jaejin.running.domain.like.SharedCourseLike; // SharedCourseLike 엔티티
import cse.jaejin.running.dto.LikeRequestDto;
import cse.jaejin.running.repository.PostLikeRepository; // PostLikeRepository 필요
import cse.jaejin.running.repository.SharedCourseLikeRepository; // SharedCourseLikeRepository 필요
import cse.jaejin.running.repository.PostRepository; // PostLike 카운트 업데이트용
import cse.jaejin.running.repository.SharedCourseRepository; // SharedCourseLike 카운트 업데이트용
import cse.jaejin.running.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostLikeRepository postLikeRepository;
    private final SharedCourseLikeRepository sharedCourseLikeRepository;
    private final PostRepository postRepository;
    private final SharedCourseRepository sharedCourseRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean toggleLike(LikeRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if ("SHARED_COURSE".equalsIgnoreCase(requestDto.getTargetType())) {
            SharedCourse sharedCourse = sharedCourseRepository.findById(requestDto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("SharedCourse not found"));

            Optional<SharedCourseLike> existingLike = sharedCourseLikeRepository.findByUserAndSharedCourse(user, sharedCourse);

            if (existingLike.isPresent()) {
                // 이미 좋아요가 있다면 취소 (삭제)
                sharedCourseLikeRepository.delete(existingLike.get());
                sharedCourse.setLikeCount(sharedCourse.getLikeCount() - 1); // 카운트 감소
                sharedCourseRepository.save(sharedCourse); // 변경사항 저장
                return false; // 좋아요 취소됨
            } else {
                // 좋아요가 없다면 추가
                SharedCourseLike newLike = new SharedCourseLike(user, sharedCourse);
                sharedCourseLikeRepository.save(newLike);
                sharedCourse.setLikeCount(sharedCourse.getLikeCount() + 1); // 카운트 증가
                sharedCourseRepository.save(sharedCourse); // 변경사항 저장
                return true; // 좋아요 추가됨
            }
        } else if ("POST".equalsIgnoreCase(requestDto.getTargetType())) {
            Post post = postRepository.findById(requestDto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));

            Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(user, post);

            if (existingLike.isPresent()) {
                // 이미 좋아요가 있다면 취소 (삭제)
                postLikeRepository.delete(existingLike.get());
                post.setLikeCount(post.getLikeCount() - 1); // 카운트 감소
                postRepository.save(post); // 변경사항 저장
                return false; // 좋아요 취소됨
            } else {
                // 좋아요가 없다면 추가
                PostLike newLike = new PostLike(user, post);
                postLikeRepository.save(newLike);
                post.setLikeCount(post.getLikeCount() + 1); // 카운트 증가
                postRepository.save(post); // 변경사항 저장
                return true; // 좋아요 추가됨
            }
        } else {
            throw new IllegalArgumentException("Invalid target type: " + requestDto.getTargetType());
        }
    }

    // 특정 게시글의 좋아요 상태를 확인하는 메서드 (사용자 로그인 시)
    public boolean isLikedByUser(Long userId, Long targetId, String targetType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if ("SHARED_COURSE".equalsIgnoreCase(targetType)) {
            SharedCourse sharedCourse = sharedCourseRepository.findById(targetId)
                    .orElseThrow(() -> new IllegalArgumentException("SharedCourse not found"));
            return sharedCourseLikeRepository.findByUserAndSharedCourse(user, sharedCourse).isPresent();
        } else if ("POST".equalsIgnoreCase(targetType)) {
            Post post = postRepository.findById(targetId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found"));
            return postLikeRepository.findByUserAndPost(user, post).isPresent();
        } else {
            throw new IllegalArgumentException("Invalid target type: " + targetType);
        }
    }

    // 특정 게시글의 좋아요 수를 조회하는 메서드 (이미 게시글 엔티티에 likeCount가 있으므로 직접 호출할 필요는 적음)
    // 하지만 좋아요 엔티티를 통해 직접 계산해야 할 필요가 있다면 유용
    public long getLikeCount(Long targetId, String targetType) {
        if ("SHARED_COURSE".equalsIgnoreCase(targetType)) {
            return sharedCourseLikeRepository.countBySharedCourseId(targetId);
        } else if ("POST".equalsIgnoreCase(targetType)) {
            return postLikeRepository.countByPostId(targetId);
        } else {
            throw new IllegalArgumentException("Invalid target type: " + targetType);
        }
    }
}