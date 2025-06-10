package cse.jaejin.running.dto;

import cse.jaejin.running.domain.CommentTargetType; // enum 임포트
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