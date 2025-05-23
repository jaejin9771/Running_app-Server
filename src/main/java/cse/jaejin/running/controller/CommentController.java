package cse.jaejin.running.controller;

import cse.jaejin.running.dto.CommentRequestDto;
import cse.jaejin.running.dto.CommentResponseDto;
import cse.jaejin.running.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> addComment(@RequestBody CommentRequestDto dto) {
        commentService.addComment(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sharedCourseId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable Long sharedCourseId) {
        return ResponseEntity.ok(commentService.getCommentsBySharedCourse(sharedCourseId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
