package cse.jaejin.running.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseDetailDto {
    private Long userId;
    private double distance;
    private int duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private List<LocationPointDto> points;

    @Data
    public static class LocationPointDto {
        private double latitude;
        private double longitude;
        private LocalDateTime timestamp;
    }
}
