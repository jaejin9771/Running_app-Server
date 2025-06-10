package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;                // 게시글 ID
    private Long userId;            // 게시글 작성자 ID
    private String username;        // 게시글 작성자 사용자 이름 (User 엔티티에 `getUsername()` 필요)
    private String title;           // 게시글 제목
    private String content;         // 게시글 내용
    private List<String> imageUrls; // 첨부된 이미지 URL 목록
    private int likeCount;          // 좋아요 수
    private int commentCount;       // 댓글 수
    private LocalDateTime createdAt;    // 게시글 생성 시간
    private LocalDateTime updatedAt;    // 게시글 마지막 업데이트 시간
}