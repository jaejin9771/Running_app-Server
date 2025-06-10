package cse.jaejin.running.controller;

import cse.jaejin.running.dto.PostRequestDto;
import cse.jaejin.running.dto.PostResponseDto;
import cse.jaejin.running.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts") // 일반 게시글 API 엔드포인트
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 특정 ID의 게시글을 조회합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * 새로운 게시글을 생성합니다.
     */
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostRequestDto requestDto) {
        Long postId = postService.createPost(requestDto);
        return new ResponseEntity<>(postId, HttpStatus.CREATED); // 생성 시 201 Created 응답
    }

    /**
     * 특정 ID의 게시글을 수정합니다.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto) {
        postService.updatePost(id, requestDto);
        return ResponseEntity.ok().build(); // 200 OK
    }

    /**
     * 특정 ID의 게시글을 삭제합니다.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * 모든 게시글 목록을 조회합니다.
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    /**
     * 특정 사용자가 작성한 게시글 목록을 조회합니다.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId));
    }
}