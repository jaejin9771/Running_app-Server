package cse.jaejin.running.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseFromRecordRequest {
    private Long userId;
    private Long runningRecordId;
    private String courseTitle;
}
