package cse.jaejin.running.service;

import cse.jaejin.running.domain.Course;
import cse.jaejin.running.domain.SharedCourse;
import cse.jaejin.running.domain.SharedCourseImage;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.SharedCourseRequestDto;
import cse.jaejin.running.dto.SharedCourseResponseDto;
import cse.jaejin.running.repository.CourseRepository;
import cse.jaejin.running.repository.SharedCourseImageRepository;
import cse.jaejin.running.repository.SharedCourseRepository;
import cse.jaejin.running.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedCourseService {

    private final SharedCourseRepository sharedCourseRepository;
    private final SharedCourseImageRepository imageRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public SharedCourseResponseDto getSharedCourseById(Long id) {
        SharedCourse course = sharedCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(course);
    }

    @Transactional
    public void deleteSharedCourse(Long id) {
        if (!sharedCourseRepository.existsById(id)) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        sharedCourseRepository.deleteById(id);
    }

    @Transactional
    public void updateSharedCourse(Long id, SharedCourseRequestDto requestDto) {
        SharedCourse course = sharedCourseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Course newCourse = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        User newUser = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        course.setCourse(newCourse);
        course.setUser(newUser);
        course.setCategory(requestDto.getCategory());
        course.setTitle(requestDto.getTitle());
        course.setContent(requestDto.getContent());

        // 기존 이미지 삭제 후 새 이미지 저장
        imageRepository.deleteAll(imageRepository.findBySharedCourseId(id));
        for (String url : requestDto.getImageUrls()) {
            SharedCourseImage image = new SharedCourseImage();
            image.setSharedCourse(course);
            image.setImageUrl(url);
            imageRepository.save(image);
        }
    }

    @Transactional
    public Long createSharedCourse(SharedCourseRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        SharedCourse sharedCourse = new SharedCourse();
        sharedCourse.setUser(user);
        sharedCourse.setCourse(course);
        sharedCourse.setCategory(requestDto.getCategory());
        sharedCourse.setTitle(requestDto.getTitle());
        sharedCourse.setContent(requestDto.getContent());

        SharedCourse saved = sharedCourseRepository.save(sharedCourse);

        for (String url : requestDto.getImageUrls()) {
            SharedCourseImage image = new SharedCourseImage();
            image.setSharedCourse(saved);
            image.setImageUrl(url);
            imageRepository.save(image);
        }

        return saved.getId();
    }

    public List<SharedCourseResponseDto> getAllSharedCourses() {
        return sharedCourseRepository.findAll().stream().map(course -> {
            SharedCourseResponseDto dto = new SharedCourseResponseDto();
            dto.setId(course.getId());
            dto.setUsername(course.getUser().getUsername()); // 필요 시
            dto.setCategory(course.getCategory());
            dto.setTitle(course.getTitle());
            dto.setContent(course.getContent());
            dto.setCreatedAt(course.getCreatedAt());

            List<String> imageUrls = imageRepository.findBySharedCourseId(course.getId())
                    .stream().map(SharedCourseImage::getImageUrl).toList();
            dto.setImageUrls(imageUrls);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<SharedCourseResponseDto> getSharedCoursesByCategory(String category) {
        return sharedCourseRepository.findByCategory(category).stream().map(this::convertToDto).toList();
    }

    public List<SharedCourseResponseDto> getSharedCoursesByUserId(Long userId) {
        return sharedCourseRepository.findByUserId(userId).stream().map(this::convertToDto).toList();
    }

    // 내부 공통 변환 메서드
    private SharedCourseResponseDto convertToDto(SharedCourse course) {
        SharedCourseResponseDto dto = new SharedCourseResponseDto();
        dto.setId(course.getId());
        dto.setUsername(course.getUser().getUsername()); // 필요시 이름 대신 id도 가능
        dto.setCategory(course.getCategory());
        dto.setTitle(course.getTitle());
        dto.setContent(course.getContent());
        dto.setCreatedAt(course.getCreatedAt());

        List<String> imageUrls = imageRepository.findBySharedCourseId(course.getId())
                .stream().map(SharedCourseImage::getImageUrl).toList();
        dto.setImageUrls(imageUrls);
        return dto;
    }
}

