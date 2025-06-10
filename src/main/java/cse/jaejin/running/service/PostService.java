package cse.jaejin.running.service;

import cse.jaejin.running.domain.Post;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.domain.Photo;
import cse.jaejin.running.domain.PhotoTargetType;
import cse.jaejin.running.dto.PostRequestDto; // PostRequestDto 필요
import cse.jaejin.running.dto.PostResponseDto; // PostResponseDto 필요
import cse.jaejin.running.repository.PostRepository;
import cse.jaejin.running.repository.PhotoRepository;
import cse.jaejin.running.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    /**
     * 특정 ID의 게시글을 조회합니다.
     * 사진 정보도 함께 DTO로 변환하여 반환합니다.
     */
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return convertToDto(post);
    }

    /**
     * 새로운 게시글을 생성합니다.
     * 게시글 정보와 함께 첨부된 사진들도 저장합니다.
     */
    @Transactional
    public Long createPost(PostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        // createdAt은 엔티티 초기화 시 자동으로 설정됩니다.
        // likeCount, commentCount는 기본값 0으로 설정됩니다.

        Post savedPost = postRepository.save(post); // 게시글 먼저 저장하여 ID 확보

        // 사진 저장
        int orderIndex = 0;
        if (requestDto.getImageUrls() != null) {
            for (String url : requestDto.getImageUrls()) {
                Photo photo = new Photo();
                photo.setTargetId(savedPost.getId()); // 저장된 Post의 ID를 targetId로 설정
                photo.setTargetType(PhotoTargetType.POST); // PhotoTargetType.POST 지정
                photo.setImageUrl(url);
                photo.setOrderIndex(orderIndex++); // 순서 부여
                photoRepository.save(photo);
            }
        }
        return savedPost.getId();
    }

    /**
     * 기존 게시글을 수정합니다.
     * 기존 사진을 모두 삭제하고 새로운 사진들로 교체합니다.
     */
    @Transactional
    public void updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 작성자 ID 변경은 일반적으로 허용되지 않으므로, 요청 DTO의 userId와 현재 게시글의 userId가 일치하는지 확인하는 로직 추가 필요.
        // 여기서는 예시를 위해 DTO의 userId로 User 객체를 다시 가져오는 코드를 남겨둡니다.
        User newUser = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        post.setUser(newUser); // 만약 작성자 변경을 허용한다면
        // 혹은, 작성자 변경을 막고 싶다면:
        // if (!post.getUser().getId().equals(requestDto.getUserId())) {
        //     throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
        // }

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        // updatedAt은 @PreUpdate 어노테이션에 의해 자동으로 갱신됩니다.

        // 기존 사진 삭제 (Post ID와 타입으로 삭제)
        photoRepository.deleteByTargetIdAndTargetType(id, PhotoTargetType.POST);

        // 새 사진 저장
        int orderIndex = 0;
        if (requestDto.getImageUrls() != null) {
            for (String url : requestDto.getImageUrls()) {
                Photo photo = new Photo();
                photo.setTargetId(post.getId()); // Post의 ID를 targetId로 설정
                photo.setTargetType(PhotoTargetType.POST); // PhotoTargetType.POST 지정
                photo.setImageUrl(url);
                photo.setOrderIndex(orderIndex++); // 순서 부여
                photoRepository.save(photo);
            }
        }
    }

    /**
     * 특정 ID의 게시글을 삭제합니다.
     * 게시글과 연관된 모든 사진도 함께 삭제됩니다.
     */
    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        // 해당 Post와 연관된 모든 사진 삭제
        photoRepository.deleteByTargetIdAndTargetType(id, PhotoTargetType.POST);
        postRepository.deleteById(id);
    }

    /**
     * 모든 게시글 목록을 조회합니다.
     * 각 게시글의 사진 정보도 함께 DTO로 변환하여 반환합니다.
     */
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto) // convertToDto 메서드 재사용
                .collect(Collectors.toList());
    }

    /**
     * 특정 사용자가 작성한 게시글 목록을 조회합니다.
     * 각 게시글의 사진 정보도 함께 DTO로 변환하여 반환합니다.
     */
    public List<PostResponseDto> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 게시글 엔티티를 응답 DTO로 변환하는 내부 공통 메서드
    private PostResponseDto convertToDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUser().getId());
        dto.setUsername(post.getUser().getUsername()); // User 엔티티에 getUsername() 필요
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setLikeCount(post.getLikeCount());
        dto.setCommentCount(post.getCommentCount());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());

        // PhotoRepository를 사용하여 해당 Post의 사진들을 조회하고 URL만 추출
        List<String> imageUrls = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(
                        post.getId(), PhotoTargetType.POST)
                .stream().map(Photo::getImageUrl).toList();
        dto.setImageUrls(imageUrls);
        return dto;
    }
}