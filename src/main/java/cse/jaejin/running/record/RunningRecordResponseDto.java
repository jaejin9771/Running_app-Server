package cse.jaejin.running.record;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class RunningRecordResponseDto {
    private Long id;
    private Long userId;
    private Long courseId;
    private String title;
    private double distance;
    private String location;
    private int duration;
    private LocalDate runDate;
    private double averageSpeed;
    private List<LocationPointDto> points;
}
