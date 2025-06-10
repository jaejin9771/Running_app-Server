package cse.jaejin.running.like;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto {
    private Long userId; // 좋아요를 누르는 사용자 ID (보안 컨텍스트에서 가져올 수도 있음)
    private Long targetId; // 좋아요 대상의 ID (SharedCourse 또는 Post의 ID)
    private String targetType; // 좋아요 대상의 타입 (SHARED_COURSE 또는 POST) - enum의 String 값
}