package cse.jaejin.running.comment;

import cse.jaejin.running.sharedCourse.SharedCourse;
import cse.jaejin.running.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor; // Lombok 사용 시 기본 생성자 추가

@Entity
@Table(name = "SharedCourseComments") // 테이블 이름은 명확하게 지정
@Getter
@Setter
@NoArgsConstructor // Lombok 사용 시 기본 생성자 추가
public class SharedCourseComment extends BaseComment { // BaseComment를 상속받음

    @ManyToOne(fetch = FetchType.LAZY) // 공유 코스 정보는 지연 로딩
    @JoinColumn(name = "sharedCourseId", nullable = false)
    private SharedCourse sharedCourse; // 이 댓글이 연결된 공유 코스

    // BaseComment의 필드들을 초기화하고 SharedCourse 필드를 추가하는 생성자
    public SharedCourseComment(User user, String content, SharedCourse sharedCourse) {
        super(user, content); // 부모 클래스의 생성자 호출
        this.sharedCourse = sharedCourse;
    }
}