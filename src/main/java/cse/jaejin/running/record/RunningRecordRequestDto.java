package cse.jaejin.running.record;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class RunningRecordRequestDto {
    private Long userId;
    private Long courseId;
    private double distance;
    private String location;
    private int duration;
    private LocalDate runDate;
    private List<LocationPointDto> points;
}
