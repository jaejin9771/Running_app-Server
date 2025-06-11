package cse.jaejin.running.post;

import cse.jaejin.running.photo.Photo;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    private String category;
    private LocalDateTime createdAt;    // 게시글 생성 시간
    private LocalDateTime updatedAt;    // 게시글 마지막 업데이트 시간

    public static PostResponseDto fromEntity(Post post, List<Photo> photos) {
        List<String> imageUrls = photos.stream()
                .sorted(Comparator.comparingInt(Photo::getOrderIndex))
                .map(Photo::getImageUrl)
                .collect(Collectors.toList());

        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setImageUrls(imageUrls);
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setCategory(post.getCategory().name());

        return dto;
    }
}
