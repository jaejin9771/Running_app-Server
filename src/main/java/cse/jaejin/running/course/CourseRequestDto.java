package cse.jaejin.running.course;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseRequestDto {
    private Long userId;
    private Long runningRecordId;
    private String courseTitle;
}
