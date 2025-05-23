package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
public class RunningRecordResponseDto {
    private Long id;
    private Long userId;
    private Long courseId;
    private double distance;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double averageSpeed;
    private List<LocationPointDto> points;
}
