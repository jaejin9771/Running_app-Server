package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class SharedCourseRequestDto {
    private Long userId;
    private Long courseId;
    private String category;
    private String title;
    private String content;
    private List<String> imageUrls;
}

