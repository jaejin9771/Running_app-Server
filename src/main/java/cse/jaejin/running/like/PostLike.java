package cse.jaejin.running.like;

import cse.jaejin.running.post.Post;
import cse.jaejin.running.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PostLikes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "postId"})
})
@Getter @Setter
@NoArgsConstructor // Lombok 사용 시 기본 생성자 추가
public class PostLike extends BaseLike { // BaseLike를 상속받음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post; // 이 좋아요가 연결된 일반 게시글

    // 필요한 경우 추가적인 생성자나 메소드 정의
    public PostLike(User user, Post post) {
        super(user); // BaseLike의 user 필드 초기화
        this.post = post;
    }
}