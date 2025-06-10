package cse.jaejin.running.sharedCourse;

import cse.jaejin.running.course.Course;
import cse.jaejin.running.user.User;
import cse.jaejin.running.photo.Photo;
import cse.jaejin.running.photo.PhotoTargetType;
import cse.jaejin.running.course.CourseRepository;
import cse.jaejin.running.photo.PhotoRepository;
import cse.jaejin.running.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SharedCourseService {

    private final SharedCourseRepository sharedCourseRepository;
    private final PhotoRepository photoRepository;
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
        // 해당 SharedCourse와 연관된 모든 사진 삭제
        photoRepository.deleteByTargetIdAndTargetType(id, PhotoTargetType.SHARED_COURSE);
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
        // SharedCourse 엔티티에 @PreUpdate가 설정되어 있다면 updatedAt은 자동으로 갱신됩니다.
        // 명시적으로 설정하려면 course.setUpdatedAt(LocalDateTime.now());를 추가할 수 있습니다.

        // 기존 사진 삭제 (SharedCourseId와 타입으로 삭제)
        photoRepository.deleteByTargetIdAndTargetType(id, PhotoTargetType.SHARED_COURSE);

        // 새 사진 저장
        int orderIndex = 0; // 사진 순서 초기화
        for (String url : requestDto.getImageUrls()) {
            Photo photo = new Photo();
            photo.setTargetId(course.getId()); // SharedCourse의 ID를 targetId로 설정
            photo.setTargetType(PhotoTargetType.SHARED_COURSE); // PhotoTargetType.SHARED_COURSE 지정
            photo.setImageUrl(url);
            photo.setOrderIndex(orderIndex++); // 순서 부여
            photoRepository.save(photo);
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
        // createdAt은 SharedCourse 엔티티의 @PrePersist 또는 초기화 로직에 따라 자동으로 설정됩니다.

        // SharedCourse를 먼저 저장하여 ID를 확보
        SharedCourse saved = sharedCourseRepository.save(sharedCourse);

        // 사진 저장
        int orderIndex = 0; // 사진 순서 초기화
        for (String url : requestDto.getImageUrls()) {
            Photo photo = new Photo();
            photo.setTargetId(saved.getId()); // 저장된 SharedCourse의 ID를 targetId로 설정
            photo.setTargetType(PhotoTargetType.SHARED_COURSE); // PhotoTargetType.SHARED_COURSE 지정
            photo.setImageUrl(url);
            photo.setOrderIndex(orderIndex++); // 순서 부여
            photoRepository.save(photo);
        }

        return saved.getId();
    }

    public List<SharedCourseResponseDto> getAllSharedCourses() {
        return sharedCourseRepository.findAll().stream().map(course -> {
            SharedCourseResponseDto dto = new SharedCourseResponseDto();
            dto.setId(course.getId());
            dto.setUsername(course.getUser().getUsername());
            dto.setCategory(course.getCategory());
            dto.setTitle(course.getTitle());
            dto.setContent(course.getContent());
            dto.setCreatedAt(course.getCreatedAt()); // createdAt 정보 추가
            dto.setUpdatedAt(course.getUpdatedAt()); // updatedAt 정보 추가

            List<String> imageUrls = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(
                            course.getId(), PhotoTargetType.SHARED_COURSE)
                    .stream().map(Photo::getImageUrl).toList();
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
        dto.setUsername(course.getUser().getUsername());
        dto.setCategory(course.getCategory());
        dto.setTitle(course.getTitle());
        dto.setContent(course.getContent());
        dto.setCreatedAt(course.getCreatedAt()); // createdAt 정보 추가
        dto.setUpdatedAt(course.getUpdatedAt()); // updatedAt 정보 추가

        List<String> imageUrls = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(
                        course.getId(), PhotoTargetType.SHARED_COURSE)
                .stream().map(Photo::getImageUrl).toList();
        dto.setImageUrls(imageUrls);
        return dto;
    }
}