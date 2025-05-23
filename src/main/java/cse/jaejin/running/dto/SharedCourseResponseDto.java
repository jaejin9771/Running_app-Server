package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class SharedCourseResponseDto {
    private Long id;
    private String username; // 사용자명 등 필요 시
    private String category;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
}

