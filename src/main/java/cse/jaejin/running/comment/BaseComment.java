package cse.jaejin.running.comment;

import cse.jaejin.running.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@MappedSuperclass // 이 클래스는 엔티티가 아니지만 매핑 정보를 자식에게 제공한다.
@Getter
@Setter
@NoArgsConstructor // Lombok 사용 시 기본 생성자 추가
public abstract class BaseComment { // 추상 클래스로 선언
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 댓글 작성자는 지연 로딩으로 효율성 높이기
    @JoinColumn(name = "userId", nullable = false)
    private User user; // 댓글 작성자

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content; // 댓글 내용

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간 (한번 설정되면 변경되지 않음)

    // updatedAt 필드와 @PreUpdate 콜백은 수정 기능 삭제로 인해 제거됨

    // 자식 클래스에서 사용할 수 있는 생성자
    public BaseComment(User user, String content) {
        this.user = user;
        this.content = content;
        // createdAt은 필드 초기화로 자동 설정
    }
}