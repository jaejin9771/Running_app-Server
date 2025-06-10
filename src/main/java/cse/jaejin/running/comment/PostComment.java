package cse.jaejin.running.comment;

import cse.jaejin.running.post.Post;
import cse.jaejin.running.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PostComments") // 테이블 이름은 명확하게 지정
@Getter
@Setter
@NoArgsConstructor
public class PostComment extends BaseComment { // BaseComment를 상속받음

    @ManyToOne(fetch = FetchType.LAZY) // 일반 게시글 정보는 지연 로딩
    @JoinColumn(name = "postId", nullable = false)
    private Post post; // 이 댓글이 연결된 일반 게시글

    // BaseComment의 필드들을 초기화하고 Post 필드를 추가하는 생성자
    public PostComment(User user, String content, Post post) {
        super(user, content); // 부모 클래스의 생성자 호출
        this.post = post;
    }
}