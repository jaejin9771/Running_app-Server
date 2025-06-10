package cse.jaejin.running.course;

import cse.jaejin.running.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Courses")
@Getter @Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String courseTitle; // 코스 이름 추가

    @Column(nullable = false)
    private double distance; // km

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseLocationPoint> points = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addPoint(CourseLocationPoint point) {
        points.add(point);
        point.setCourse(this);
    }
}
