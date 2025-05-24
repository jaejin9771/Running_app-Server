package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
public class RunningRecordResponseDto {
    private Long id;
    private Long userId;
    private Long courseId;
    private double distance;
    private int duration;
    private LocalDate runDate;
    private double averageSpeed;
    private List<LocationPointDto> points;
}
