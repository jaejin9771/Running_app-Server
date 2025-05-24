package cse.jaejin.running.service;

import cse.jaejin.running.domain.*;
import cse.jaejin.running.dto.*;
import cse.jaejin.running.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final RunningRecordRepository runningRecordRepository;

    /**
     * 러닝 기록(RunningRecord)을 기반으로 코스를 생성하는 메서드
     */
    @Transactional
    public Long createCourseFromRunningRecord(CourseRequestDto request) {
        // 사용자 정보 조회 (예외 발생 시 404 대체용 예외 처리)
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 러닝 기록 조회
        RunningRecord record = runningRecordRepository.findById(request.getRunningRecordId())
                .orElseThrow(() -> new IllegalArgumentException("RunningRecord not found"));

        // 새로운 Course 엔티티 생성
        Course course = new Course();
        course.setUser(user);  // 사용자 설정
        course.setDistance(calculateDistance(record));  // 거리 계산 (추후 구현 필요)
        course.setCourseTitle(request.getCourseTitle());
        course.setLocation(record.getLocation());

        // 러닝 기록의 좌표를 CourseLocationPoint로 복사하여 Course에 추가
        for (RunningLocationPoint rp : record.getPoints()) {
            CourseLocationPoint cp = new CourseLocationPoint();
            cp.setLatitude(rp.getLatitude());
            cp.setLongitude(rp.getLongitude());
            course.addPoint(cp);  // 연관관계 편의 메서드로 연결
        }

        // 저장 후 생성된 코스의 ID 반환
        return courseRepository.save(course).getId();
    }

    /**
     * 러닝 기록으로부터 총 거리를 계산하는 로직 (현재는 더미값, 추후 좌표 거리 계산 알고리즘 적용 예정)
     */
    private double calculateDistance(RunningRecord record) {
        return 0.0; // TODO: Haversine 공식을 이용한 거리 계산 추가 예정
    }

    /**
     * ID로 단일 코스를 조회하여 DTO로 변환
     */
    @Transactional(readOnly = true)
    public CourseResponseDto getCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        return convertToDto(course);
    }

    /**
     * 전체 코스 목록을 조회하여 DTO 리스트로 반환
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자(userId)가 생성한 코스 목록을 조회
     */
    @Transactional(readOnly = true)
    public List<CourseResponseDto> getCoursesByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return courseRepository.findByUser(user).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CourseResponseDto> searchByCourseTitle(String courseTitle) {
        List<Course> courses = courseRepository.findByCourseTitleContainingIgnoreCase(courseTitle);
        return courses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Course 엔티티를 CourseResponseDto로 변환하는 내부 메서드
     */
    private CourseResponseDto convertToDto(Course course) {
        CourseResponseDto dto = new CourseResponseDto();
        dto.setId(course.getId());
        dto.setUserId(course.getUser().getId());
        dto.setDistance(course.getDistance());
        dto.setCourseTitle(course.getCourseTitle());
        dto.setLocation(course.getLocation());

        // 위치 좌표 리스트를 DTO로 변환
        List<LocationPointDto> pointDtos = course.getPoints().stream().map(p -> {
            LocationPointDto pd = new LocationPointDto();
            pd.setLatitude(p.getLatitude());
            pd.setLongitude(p.getLongitude());
            return pd;
        }).collect(Collectors.toList());

        dto.setPoints(pointDtos);
        return dto;
    }
}
