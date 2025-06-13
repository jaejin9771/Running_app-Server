package cse.jaejin.running.record;

import cse.jaejin.running.course.Course;
import cse.jaejin.running.course.CourseRepository;
import cse.jaejin.running.user.User;
import cse.jaejin.running.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunningRecordService {

    private final RunningRecordRepository runningRecordRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public Long createRunningRecord(RunningRecordRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = null;
        if (dto.getCourseId() != null) {
            course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        }

        RunningRecord record = new RunningRecord();
        record.setUser(user);
        record.setCourse(course);
        record.setDistance(dto.getDistance());
        record.setDuration(dto.getDuration());
        record.setRunDate(dto.getRunDate());
        record.setLocation(dto.getLocation());

        if (dto.getPoints() != null) {
            for (LocationPointDto pointDto : dto.getPoints()) {
                RunningLocationPoint point = new RunningLocationPoint();
                point.setLatitude(pointDto.getLatitude());
                point.setLongitude(pointDto.getLongitude());
                point.setTimestamp(pointDto.getTimestamp());
                record.addPoint(point);
            }
        }

        return runningRecordRepository.save(record).getId();
    }

    @Transactional(readOnly = true)
    public List<RunningRecordResponseDto> getRecordsByUser(Long userId) {
        return runningRecordRepository.findByUserId(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RunningRecordResponseDto getRunningRecord(Long id) {
        RunningRecord record = runningRecordRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found"));
        return toDto(record);
    }

    @Transactional
    public void deleteRunningRecord(Long id) {
        runningRecordRepository.deleteById(id);
    }

    private RunningRecordResponseDto toDto(RunningRecord record) {
        RunningRecordResponseDto dto = new RunningRecordResponseDto();
        dto.setId(record.getId());
        dto.setUserId(record.getUser().getId());
        dto.setCourseId(record.getCourse() != null ? record.getCourse().getId() : null);
        dto.setDistance(record.getDistance());
        dto.setDuration(record.getDuration());
        dto.setRunDate(record.getRunDate());
        dto.setLocation(record.getLocation());
        dto.setAverageSpeed(record.getAverageSpeed());

        List<LocationPointDto> points = record.getPoints().stream().map(p -> {
            LocationPointDto pd = new LocationPointDto();
            pd.setLatitude(p.getLatitude());
            pd.setLongitude(p.getLongitude());
            pd.setTimestamp(p.getTimestamp());
            return pd;
        }).collect(Collectors.toList());

        dto.setPoints(points);
        return dto;
    }
}
