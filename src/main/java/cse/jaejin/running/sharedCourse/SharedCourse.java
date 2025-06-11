package cse.jaejin.running.sharedCourse;

import cse.jaejin.running.course.Course;
import cse.jaejin.running.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "SharedCourses")
@Getter @Setter
@NoArgsConstructor // Lombok 사용 시 기본 생성자 추가
public class SharedCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 사용자 정보는 지연 로딩
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) // 코스 정보는 지연 로딩
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 업데이트 시간 추가 (필요 시)

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- 좋아요 및 댓글 수 (캐싱 목적) ---
    @Column(nullable = false)
    private int likeCount = 0;

    @Column(nullable = false)
    private int commentCount = 0;
}
