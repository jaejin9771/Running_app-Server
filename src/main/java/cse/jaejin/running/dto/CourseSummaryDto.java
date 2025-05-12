package cse.jaejin.running.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseSummaryDto {
    private Long userId;
    private double distance;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
