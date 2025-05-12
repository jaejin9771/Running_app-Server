package cse.jaejin.running.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "Likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "sharedCourseId", nullable = false)
    private SharedCourse sharedCourse;

    // 같은 사용자가 같은 공유글에 여러 번 좋아요를 누르지 않도록 설정
    @Column(unique = true)
    private String uniqueKey;

    @PrePersist
    public void generateUniqueKey() {
        this.uniqueKey = user.getId() + "_" + sharedCourse.getId();
    }

    // Getter, Setter, Constructor
}
