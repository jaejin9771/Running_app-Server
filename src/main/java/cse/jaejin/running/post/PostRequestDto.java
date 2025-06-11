package cse.jaejin.running.post;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private Long userId;        // 게시글 작성자 ID
    private String title;       // 게시글 제목
    private String content;     // 게시글 내용
    private List<String> imageUrls; // 첨부될 이미지 URL 목록
    private String category;
}