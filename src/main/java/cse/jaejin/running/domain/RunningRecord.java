package cse.jaejin.running.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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
    @JoinColumn(name = "courseId")
    private Course course;

    // 총 거리 (단위: km)
    @Column(nullable = false)
    private double distance;

    // 소요 시간 (초 단위)
    @Column(nullable = false)
    private int duration;

    // 달린 날짜
    @Column(nullable = false)
    private LocalDate runDate;

    @OneToMany(mappedBy = "runningRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RunningLocationPoint> points = new ArrayList<>();

    public void addPoint(RunningLocationPoint point) {
        points.add(point);
        point.setRunningRecord(this);
    }

    // 평균 속도 계산 (단위: km/h)
    public double getAverageSpeed() {
        if (duration == 0) return 0.0;
        return (distance / duration) * 3600;
    }
}
