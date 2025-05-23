package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequestDto {
    private Long userId;
    private Long sharedCourseId;
    private String content;
}

