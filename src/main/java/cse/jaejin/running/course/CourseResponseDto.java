package cse.jaejin.running.course;

import cse.jaejin.running.record.LocationPointDto;
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
