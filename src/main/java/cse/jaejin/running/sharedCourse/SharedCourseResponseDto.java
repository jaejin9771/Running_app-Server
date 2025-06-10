package cse.jaejin.running.sharedCourse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 추가
public class SharedCourseResponseDto {
    private Long id;                // 게시글 ID
    private Long userId;            // 게시글 작성자 ID
    private String username;        // 게시글 작성자 사용자 이름 (편의상 추가)
    private Long courseId;          // 공유된 코스 ID
    private String courseName;      // 공유된 코스 이름 (편의상 추가)
    private String category;        // 게시글 카테고리
    private String title;           // 게시글 제목
    private String content;         // 게시글 내용
    private List<String> imageUrls; // 첨부된 이미지 URL 목록
    private int likeCount;          // 좋아요 수
    private int commentCount;       // 댓글 수
    private LocalDateTime createdAt;    // 게시글 생성 시간
    private LocalDateTime updatedAt;    // 게시글 마지막 업데이트 시간
}

