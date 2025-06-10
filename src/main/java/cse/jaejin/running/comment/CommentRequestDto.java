package cse.jaejin.running.comment;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private Long userId;
    private Long targetId;
    private CommentTargetType targetType;
    private String content;
}