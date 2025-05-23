package cse.jaejin.running.service;

import cse.jaejin.running.domain.Comment;
import cse.jaejin.running.domain.SharedCourse;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.CommentRequestDto;
import cse.jaejin.running.dto.CommentResponseDto;
import cse.jaejin.running.repository.CommentRepository;
import cse.jaejin.running.repository.SharedCourseRepository;
import cse.jaejin.running.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SharedCourseRepository sharedCourseRepository;

    @Transactional
    public void addComment(CommentRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        SharedCourse sharedCourse = sharedCourseRepository.findById(dto.getSharedCourseId())
                .orElseThrow(() -> new IllegalArgumentException("SharedCourse not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setSharedCourse(sharedCourse);
        comment.setContent(dto.getContent());

        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getCommentsBySharedCourse(Long sharedCourseId) {
        return commentRepository.findBySharedCourseIdOrderByCreatedAtDesc(sharedCourseId)
                .stream()
                .map(comment -> {
                    CommentResponseDto dto = new CommentResponseDto();
                    dto.setId(comment.getId());
                    dto.setUserId(comment.getUser().getId());
                    dto.setUsername(comment.getUser().getUsername());
                    dto.setContent(comment.getContent());
                    dto.setCreatedAt(comment.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
