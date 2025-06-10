package cse.jaejin.running.like;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes") // 좋아요 관련 엔드포인트
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요 토글 (추가 또는 취소)
     * 요청 본문: { "userId": 1, "targetId": 10, "targetType": "SHARED_COURSE" }
     * 혹은 { "userId": 1, "targetId": 20, "targetType": "POST" }
     */
    @PostMapping("/toggle")
    public ResponseEntity<String> toggleLike(@RequestBody LikeRequestDto requestDto) {
        boolean isLiked = likeService.toggleLike(requestDto);
        if (isLiked) {
            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        } else {
            return ResponseEntity.ok("좋아요가 취소되었습니다.");
        }
    }

    /**
     * 특정 게시글의 좋아요 상태 확인 (사용자가 좋아요를 눌렀는지)
     * GET /api/likes/status?userId=1&targetId=10&targetType=SHARED_COURSE
     */
    @GetMapping("/status")
    public ResponseEntity<Boolean> getLikeStatus(
            @RequestParam Long userId,
            @RequestParam Long targetId,
            @RequestParam String targetType) {
        boolean isLiked = likeService.isLikedByUser(userId, targetId, targetType);
        return ResponseEntity.ok(isLiked);
    }

    /**
     * 특정 게시글의 좋아요 수 조회
     * GET /api/likes/count?targetId=10&targetType=SHARED_COURSE
     * (이 정보는 Post/SharedCourse 응답 DTO에 포함되어 있으므로 직접 호출할 일은 적을 수 있습니다.)
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getLikeCount(
            @RequestParam Long targetId,
            @RequestParam String targetType) {
        long count = likeService.getLikeCount(targetId, targetType);
        return ResponseEntity.ok(count);
    }
}