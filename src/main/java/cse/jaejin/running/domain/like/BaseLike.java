package cse.jaejin.running.domain.like;
import cse.jaejin.running.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@MappedSuperclass // 이 클래스는 엔티티가 아니지만 매핑 정보를 자식에게 제공한다.
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor // Lombok 사용 시 편리
public abstract class BaseLike { // 추상 클래스로 선언하여 직접 인스턴스화 방지
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 좋아요를 누른 사용자는 지연 로딩으로 효율성 높이기
    @JoinColumn(name = "userId", nullable = false)
    private User user; // 좋아요를 누른 사용자

    public BaseLike(User user) {
        this.user = user;
    }
}
