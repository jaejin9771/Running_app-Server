package cse.jaejin.running.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시글 단건 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * 게시글 생성
     */
    @PostMapping
    public ResponseEntity<Long> createPost(@RequestBody PostRequestDto requestDto) {
        Long postId = postService.createPost(requestDto);
        return new ResponseEntity<>(postId, HttpStatus.CREATED);
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePost(
            @PathVariable Long id,
            @RequestBody PostRequestDto requestDto) {
        postService.updatePost(id, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 모든 게시글 조회
     */
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    /**
     * 사용자별 게시글 조회
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(postService.getPostsByUserId(userId));
    }

    /**
     * 카테고리별 게시글 조회
     */
    @GetMapping("/category")
    public ResponseEntity<List<PostResponseDto>> getPostsByCategory(@RequestParam String category) {
        return ResponseEntity.ok(postService.findByCategory(category));
    }
}
