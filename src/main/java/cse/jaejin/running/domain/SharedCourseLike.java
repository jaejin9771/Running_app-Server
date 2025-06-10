package cse.jaejin.running.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SharedCourseLikes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "sharedCourseId"})
})
@Getter @Setter
@NoArgsConstructor // Lombok 사용 시 기본 생성자 추가
public class SharedCourseLike extends BaseLike { // BaseLike를 상속받음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sharedCourseId", nullable = false)
    private SharedCourse sharedCourse; // 이 좋아요가 연결된 공유 코스

    // 필요한 경우 추가적인 생성자나 메소드 정의
    public SharedCourseLike(User user, SharedCourse sharedCourse) {
        super(user); // BaseLike의 user 필드 초기화
        this.sharedCourse = sharedCourse;
    }
}