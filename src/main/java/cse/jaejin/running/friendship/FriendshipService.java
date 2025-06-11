package cse.jaejin.running.friendship;

import cse.jaejin.running.user.User;
import cse.jaejin.running.user.UserRepository;
import cse.jaejin.running.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
        friendshipRepository.findByUserAndFriend(reverse.getUser(), reverse.getFriend())
                .ifPresentOrElse(
                        existing -> {
                            if (existing.getStatus() == FriendshipStatus.REQUESTED) {
                                existing.setStatus(FriendshipStatus.ACCEPTED);
                                friendshipRepository.save(existing);
                            }
                        },
                        () -> friendshipRepository.save(reverse)
                );
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

    @Transactional(readOnly = true)
    public List<FriendshipResponseDto> getSentFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return friendshipRepository.findByUser(user).stream()
                .filter(f -> f.getStatus() == FriendshipStatus.REQUESTED)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FriendshipResponseDto> getReceivedFriendRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        return friendshipRepository.findByFriend(user).stream()
                .filter(f -> f.getStatus() == FriendshipStatus.REQUESTED)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteFriendship(Long userId, Long friendId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new IllegalArgumentException("친구 없음"));

        // 양방향 관계 삭제
        friendshipRepository.findByUserAndFriend(user, friend)
                .ifPresent(friendshipRepository::delete);

        friendshipRepository.findByUserAndFriend(friend, user)
                .ifPresent(friendshipRepository::delete);
    }

    @Transactional
    public void rejectFriendRequest(Long id) {
        Friendship request = friendshipRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("요청 없음"));

        // 상태가 REQUESTED인 경우에만 거절 가능
        if (request.getStatus() != FriendshipStatus.REQUESTED) {
            throw new IllegalStateException("이미 수락되었거나 유효하지 않은 요청입니다.");
        }

        friendshipRepository.delete(request);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> searchAvailableUsers(Long requesterId, String keyword) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // keyword로 검색된 유저들 중
        List<User> allMatchingUsers = userRepository.findByUsernameContainingIgnoreCase(keyword);

        // 이미 친구 요청을 보냈거나 받은 사용자들
        List<Friendship> relations = friendshipRepository.findAllByUserOrFriend(requester, requester);

        Set<Long> excludedUserIds = relations.stream()
                .map(f -> {
                    if (f.getUser().getId().equals(requesterId)) return f.getFriend().getId();
                    else return f.getUser().getId();
                })
                .collect(Collectors.toSet());
        excludedUserIds.add(requesterId); // 본인도 제외

        return allMatchingUsers.stream()
                .filter(user -> !excludedUserIds.contains(user.getId()))
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
