package cse.jaejin.running.domain;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "RunningRecords")
@Getter @Setter
public class RunningRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 사용자가
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 어떤 코스를 달렸는지
    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    // 소요 시간 (초 단위)
    @Column(nullable = false)
    private int duration;

    // 시작 시각
    @Column(nullable = false)
    private LocalDateTime startTime;

    // 종료 시각
    @Column(nullable = false)
    private LocalDateTime endTime;

    @OneToMany(mappedBy = "runningRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RunningLocationPoint> points = new ArrayList<>();

    public void addPoint(RunningLocationPoint point) {
        points.add(point);
        point.setRunningRecord(this);
    }
}
