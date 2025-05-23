package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
