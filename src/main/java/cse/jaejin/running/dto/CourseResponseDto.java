package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CourseResponseDto {
    private Long id;
    private String courseTitle;
    private Long userId;
    private double distance;
    private String location;
    private List<LocationPointDto> points;
}
