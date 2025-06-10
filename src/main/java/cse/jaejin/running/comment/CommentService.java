package cse.jaejin.running.comment;

import cse.jaejin.running.post.Post;
import cse.jaejin.running.post.PostRepository;
import cse.jaejin.running.sharedCourse.SharedCourse;
import cse.jaejin.running.sharedCourse.SharedCourseRepository;
import cse.jaejin.running.user.User;
import cse.jaejin.running.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserRepository userRepository;
    private final SharedCourseRepository sharedCourseRepository;
    private final PostRepository postRepository;
    private final SharedCourseCommentRepository sharedCourseCommentRepository;
    private final PostCommentRepository postCommentRepository;

    /**
     * 새로운 댓글을 생성합니다.
     * 대상(SharedCourse 또는 Post)의 commentCount를 증가시킵니다.
     */
    @Transactional
    public Long createComment(CommentRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + requestDto.getUserId()));

        // enum 타입으로 비교
        if (requestDto.getTargetType() == CommentTargetType.SHARED_COURSE) {
            SharedCourse sharedCourse = sharedCourseRepository.findById(requestDto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("SharedCourse not found with ID: " + requestDto.getTargetId()));

            SharedCourseComment comment = new SharedCourseComment(user, requestDto.getContent(), sharedCourse);
            SharedCourseComment savedComment = sharedCourseCommentRepository.save(comment);

            sharedCourse.setCommentCount(sharedCourse.getCommentCount() + 1);
            sharedCourseRepository.save(sharedCourse);

            return savedComment.getId();

        } else if (requestDto.getTargetType() == CommentTargetType.POST) {
            Post post = postRepository.findById(requestDto.getTargetId())
                    .orElseThrow(() -> new IllegalArgumentException("Post not found with ID: " + requestDto.getTargetId()));

            PostComment comment = new PostComment(user, requestDto.getContent(), post);
            PostComment savedComment = postCommentRepository.save(comment);

            post.setCommentCount(post.getCommentCount() + 1);
            postRepository.save(post);

            return savedComment.getId();

        } else {
            throw new IllegalArgumentException("Invalid target type for comment creation: " + requestDto.getTargetType());
        }
    }

    /**
     * 특정 ID의 댓글을 삭제합니다.
     * 대상(SharedCourse 또는 Post)의 commentCount를 감소시킵니다.
     */
    @Transactional
    public void deleteComment(Long commentId, Long userId, CommentTargetType targetType, Long targetId) { // targetType 변경
        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // enum 타입으로 비교
        if (targetType == CommentTargetType.SHARED_COURSE) {
            SharedCourseComment comment = sharedCourseCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("SharedCourse comment not found: " + commentId));

            if (!comment.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
            }

            sharedCourseCommentRepository.delete(comment);

            SharedCourse sharedCourse = sharedCourseRepository.findById(targetId)
                    .orElseThrow(() -> new IllegalArgumentException("SharedCourse not found: " + targetId));
            sharedCourse.setCommentCount(sharedCourse.getCommentCount() - 1);
            sharedCourseRepository.save(sharedCourse);

        } else if (targetType == CommentTargetType.POST) {
            PostComment comment = postCommentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("Post comment not found: " + commentId));

            if (!comment.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
            }

            postCommentRepository.delete(comment);

            Post post = postRepository.findById(targetId)
                    .orElseThrow(() -> new IllegalArgumentException("Post not found: " + targetId));
            post.setCommentCount(post.getCommentCount() - 1);
            postRepository.save(post);

        } else {
            throw new IllegalArgumentException("Invalid target type for comment deletion: " + targetType);
        }
    }

    /**
     * 특정 대상(SharedCourse 또는 Post)의 모든 댓글을 조회합니다.
     */
    public List<CommentResponseDto> getCommentsByTargetId(Long targetId, CommentTargetType targetType) {
        if (targetType == CommentTargetType.SHARED_COURSE) {
            // SharedCourseCommentRepository에 직접 targetId를 전달하여 댓글 조회 (DB 조회 1회)
            return sharedCourseCommentRepository.findBySharedCourseIdOrderByCreatedAtAsc(targetId).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else if (targetType == CommentTargetType.POST) {
            // PostCommentRepository에 직접 targetId를 전달하여 댓글 조회 (DB 조회 1회)
            return postCommentRepository.findByPostIdOrderByCreatedAtAsc(targetId).stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Invalid target type for comment retrieval: " + targetType);
        }
    }

    /**
     * BaseComment 엔티티를 CommentResponseDto로 변환하는 단일 공통 메서드.
     * 엔티티의 실제 타입에 따라 targetId와 targetType을 설정합니다.
     */
    private CommentResponseDto convertToDto(BaseComment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUser().getId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());

        // Comment의 실제 타입에 따라 targetId와 targetType을 결정
        if (comment instanceof SharedCourseComment) {
            SharedCourseComment sharedCourseComment = (SharedCourseComment) comment;
            dto.setTargetId(sharedCourseComment.getSharedCourse().getId());
            dto.setTargetType(CommentTargetType.SHARED_COURSE);
        } else if (comment instanceof PostComment) {
            PostComment postComment = (PostComment) comment;
            dto.setTargetId(postComment.getPost().getId());
            dto.setTargetType(CommentTargetType.POST);
        } else {
            // 예상치 못한 타입의 BaseComment가 들어올 경우 (발생할 가능성은 낮음)
            throw new IllegalArgumentException("Unknown comment type: " + comment.getClass().getName());
        }
        return dto;
    }
}