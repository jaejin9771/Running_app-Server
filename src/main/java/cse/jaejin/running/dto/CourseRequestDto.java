package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CourseRequestDto {
    private Long userId;
    private String courseTitle;
    private double distance;
    private List<LocationPointDto> points;
}
