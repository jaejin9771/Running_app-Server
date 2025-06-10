package cse.jaejin.running.controller;

import cse.jaejin.running.domain.CommentTargetType; // CommentTargetType 임포트
import cse.jaejin.running.dto.CommentRequestDto;
import cse.jaejin.running.dto.CommentResponseDto;
import cse.jaejin.running.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 새로운 댓글을 생성합니다.
     * 클라이언트는 댓글을 달 대상의 ID와 타입, 그리고 내용을 제공합니다.
     * POST /api/comments
     * 요청 본문 예시:
     * {
     * "userId": 1,
     * "targetId": 10,
     * "targetType": "SHARED_COURSE", // String 값으로 enum 이름 전달
     * "content": "정말 좋은 코스네요!"
     * }
     */
    @PostMapping
    public ResponseEntity<Long> createComment(@RequestBody CommentRequestDto requestDto) {
        Long commentId = commentService.createComment(requestDto);
        return new ResponseEntity<>(commentId, HttpStatus.CREATED);
    }

    /**
     * 특정 ID의 댓글을 삭제합니다.
     * DELETE /api/comments/{commentId}?userId=1&targetType=SHARED_COURSE&targetId=10
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestParam CommentTargetType targetType,
            @RequestParam Long targetId) {
        commentService.deleteComment(commentId, userId, targetType, targetId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 특정 대상(SharedCourse 또는 Post)에 달린 모든 댓글을 조회합니다.
     * GET /api/comments?targetId=10&targetType=SHARED_COURSE
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getCommentsByTarget(
            @RequestParam Long targetId,
            @RequestParam CommentTargetType targetType) {
        List<CommentResponseDto> comments = commentService.getCommentsByTargetId(targetId, targetType);
        return ResponseEntity.ok(comments);
    }
}