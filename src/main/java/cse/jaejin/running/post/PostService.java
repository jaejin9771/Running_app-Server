package cse.jaejin.running.post;

import cse.jaejin.running.user.User;
import cse.jaejin.running.photo.Photo;
import cse.jaejin.running.photo.PhotoTargetType;
import cse.jaejin.running.photo.PhotoRepository;
import cse.jaejin.running.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
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
     */
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        List<Photo> photos = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(id, PhotoTargetType.POST);
        return PostResponseDto.fromEntity(post, photos);
    }

    /**
     * 새로운 게시글을 생성합니다.
     */
    @Transactional
    public Long createPost(PostRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostCategory category;
        try {
            category = PostCategory.valueOf(requestDto.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }

        Post post = new Post();
        post.setUser(user);
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setCategory(category);

        Post savedPost = postRepository.save(post);

        // 사진 저장
        int orderIndex = 0;
        if (requestDto.getImageUrls() != null) {
            for (String url : requestDto.getImageUrls()) {
                Photo photo = new Photo();
                photo.setTargetId(savedPost.getId());
                photo.setTargetType(PhotoTargetType.POST);
                photo.setImageUrl(url);
                photo.setOrderIndex(orderIndex++);
                photoRepository.save(photo);
            }
        }

        return savedPost.getId();
    }

    /**
     * 기존 게시글을 수정합니다.
     */
    @Transactional
    public void updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        post.setUser(user);

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());

        PostCategory category;
        try {
            category = PostCategory.valueOf(requestDto.getCategory());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다.");
        }
        post.setCategory(category);

        // 기존 사진 삭제
        photoRepository.deleteByTargetIdAndTargetType(id, PhotoTargetType.POST);

        // 새 사진 저장
        int orderIndex = 0;
        if (requestDto.getImageUrls() != null) {
            for (String url : requestDto.getImageUrls()) {
                Photo photo = new Photo();
                photo.setTargetId(post.getId());
                photo.setTargetType(PhotoTargetType.POST);
                photo.setImageUrl(url);
                photo.setOrderIndex(orderIndex++);
                photoRepository.save(photo);
            }
        }
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("게시글이 존재하지 않습니다.");
        }
        photoRepository.deleteByTargetIdAndTargetType(id, PhotoTargetType.POST);
        postRepository.deleteById(id);
    }

    /**
     * 전체 게시글 조회
     */
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> {
                    List<Photo> photos = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(post.getId(), PhotoTargetType.POST);
                    return PostResponseDto.fromEntity(post, photos);
                })
                .collect(Collectors.toList());
    }

    /**
     * 사용자별 게시글 조회
     */
    public List<PostResponseDto> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(post -> {
                    List<Photo> photos = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(post.getId(), PhotoTargetType.POST);
                    return PostResponseDto.fromEntity(post, photos);
                })
                .collect(Collectors.toList());
    }

    /**
     * 카테고리별 게시글 조회
     */
    public List<PostResponseDto> findByCategory(String categoryName) {
        PostCategory category;
        try {
            category = PostCategory.valueOf(categoryName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 카테고리입니다: " + categoryName);
        }

        List<Post> posts = postRepository.findByCategory(category);
        return posts.stream()
                .map(post -> {
                    List<Photo> photos = photoRepository.findByTargetIdAndTargetTypeOrderByOrderIndexAsc(post.getId(), PhotoTargetType.POST);
                    return PostResponseDto.fromEntity(post, photos);
                })
                .collect(Collectors.toList());
    }
}
