package cse.jaejin.running.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private Long targetId;
    private CommentTargetType targetType;
    private String content;
    private LocalDateTime createdAt;
}