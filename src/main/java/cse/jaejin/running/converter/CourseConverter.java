package cse.jaejin.running.converter;

import cse.jaejin.running.domain.Course;
import cse.jaejin.running.domain.LocationPoint;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.CourseDetailDto;
import cse.jaejin.running.dto.CourseDto;
import cse.jaejin.running.dto.CourseSummaryDto;

import java.util.List;
import java.util.stream.Collectors;

public class CourseConverter {

    // Course → CourseSummaryDto
    public static CourseSummaryDto toSummaryDto(Course course) {
        CourseSummaryDto dto = new CourseSummaryDto();
        dto.setUserId(course.getUser().getId());
        dto.setDistance(course.getDistance());
        dto.setDuration(course.getDuration());
        dto.setStartTime(course.getStartTime());
        dto.setEndTime(course.getEndTime());
        return dto;
    }

    // Course → CourseDetailDto
    public static CourseDetailDto toDetailDto(Course course) {
        CourseDetailDto dto = new CourseDetailDto();
        dto.setUserId(course.getUser().getId());
        dto.setDistance(course.getDistance());
        dto.setDuration(course.getDuration());
        dto.setStartTime(course.getStartTime());
        dto.setEndTime(course.getEndTime());

        List<CourseDetailDto.LocationPointDto> points = course.getPoints().stream()
                .map(CourseConverter::toLocationPointDto)
                .collect(Collectors.toList());

        dto.setPoints(points);
        return dto;
    }

    // LocationPoint → LocationPointDto (내부에서만 사용)
    private static CourseDetailDto.LocationPointDto toLocationPointDto(LocationPoint point) {
        CourseDetailDto.LocationPointDto dto = new CourseDetailDto.LocationPointDto();
        dto.setLatitude(point.getLatitude());
        dto.setLongitude(point.getLongitude());
        dto.setTimestamp(point.getTimestamp());
        return dto;
    }

    public static Course toEntity(CourseDto dto, User user) {
        Course course = new Course();
        course.setUser(user);  // userId → User 조회 후 주입
        course.setDistance(dto.getDistance());
        course.setDuration(dto.getDuration());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());

        if (dto.getPoints() != null) {
            for (CourseDto.LocationPointDto pdto : dto.getPoints()) {
                LocationPoint point = new LocationPoint();
                point.setLatitude(pdto.getLatitude());
                point.setLongitude(pdto.getLongitude());
                point.setTimestamp(pdto.getTimestamp());
                course.addPoint(point);  // 연관관계 설정
            }
        }

        return course;
    }

}
