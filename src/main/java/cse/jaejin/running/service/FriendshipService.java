package cse.jaejin.running.service;

import cse.jaejin.running.domain.Friendship;
import cse.jaejin.running.domain.FriendshipStatus;
import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.FriendshipRequestDto;
import cse.jaejin.running.dto.FriendshipResponseDto;
import cse.jaejin.running.repository.FriendshipRepository;
import cse.jaejin.running.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public FriendshipResponseDto sendFriendRequest(FriendshipRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        User friend = userRepository.findById(dto.getFriendId())
                .orElseThrow(() -> new IllegalArgumentException("친구 대상 없음"));

        // 중복 요청 방지
        friendshipRepository.findByUserAndFriend(user, friend)
                .ifPresent(f -> { throw new IllegalStateException("이미 친구 요청됨"); });

        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setStatus(FriendshipStatus.REQUESTED);
        friendship.setCreatedAt(java.time.LocalDateTime.now());

        friendshipRepository.save(friendship);

        return toDto(friendship);
    }

    @Transactional
    public void acceptFriendRequest(Long id) {
        Friendship request = friendshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청 없음"));

        request.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(request);

        // 반대 방향으로도 저장
        Friendship reverse = new Friendship();
        reverse.setUser(request.getFriend()); // 친구가 now 요청자
        reverse.setFriend(request.getUser()); // 나를 친구로 저장
        reverse.setStatus(FriendshipStatus.ACCEPTED);
        reverse.setCreatedAt(LocalDateTime.now());

        // 중복 방지
        if (friendshipRepository.findByUserAndFriend(reverse.getUser(), reverse.getFriend()).isEmpty()) {
            friendshipRepository.save(reverse);
        }
    }


    @Transactional(readOnly = true)
    public List<FriendshipResponseDto> getFriendList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return friendshipRepository.findByUser(user).stream()
                .filter(f -> f.getStatus() == FriendshipStatus.ACCEPTED)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private FriendshipResponseDto toDto(Friendship friendship) {
        FriendshipResponseDto dto = new FriendshipResponseDto();
        dto.setId(friendship.getId());
        dto.setUserId(friendship.getUser().getId());
        dto.setFriendId(friendship.getFriend().getId());
        dto.setStatus(friendship.getStatus());
        dto.setCreatedAt(friendship.getCreatedAt());
        return dto;
    }
}
