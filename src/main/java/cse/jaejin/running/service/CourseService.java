package cse.jaejin.running.service;

import cse.jaejin.running.domain.Course;
import cse.jaejin.running.domain.LocationPoint;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.CourseDetailDto;
import cse.jaejin.running.dto.CourseDto;
import cse.jaejin.running.repository.CourseRepository;
import cse.jaejin.running.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public Optional<Long> saveCourse(CourseDto dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        if (userOpt.isEmpty()) return Optional.empty();

        Course course = new Course();
        course.setUser(userOpt.get());
        course.setDistance(dto.getDistance());
        course.setDuration(dto.getDuration());
        course.setStartTime(dto.getStartTime());
        course.setEndTime(dto.getEndTime());

        if (dto.getPoints() != null) {
            for (CourseDto.LocationPointDto p : dto.getPoints()) {
                LocationPoint point = new LocationPoint();
                point.setLatitude(p.getLatitude());
                point.setLongitude(p.getLongitude());
                point.setTimestamp(p.getTimestamp());
                course.addPoint(point);
            }
        }

        Course saved = courseRepository.save(course);
        return Optional.of(saved.getId());
    }

    public CourseDetailDto convertToDetailDto(Course course) {
        CourseDetailDto dto = new CourseDetailDto();
        dto.setUserId(course.getUser().getId());
        dto.setDistance(course.getDistance());
        dto.setDuration(course.getDuration());
        dto.setStartTime(course.getStartTime());
        dto.setEndTime(course.getEndTime());

        List<CourseDetailDto.LocationPointDto> pointDtos = course.getPoints().stream()
                .map(point -> {
                    CourseDetailDto.LocationPointDto pdto = new CourseDetailDto.LocationPointDto();
                    pdto.setLatitude(point.getLatitude());
                    pdto.setLongitude(point.getLongitude());
                    pdto.setTimestamp(point.getTimestamp());
                    return pdto;
                })
                .toList();

        dto.setPoints(pointDtos);
        return dto;
    }
}
