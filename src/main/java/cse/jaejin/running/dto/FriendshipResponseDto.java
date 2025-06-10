package cse.jaejin.running.dto;

import cse.jaejin.running.domain.FriendshipStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class FriendshipResponseDto {
    private Long id;
    private Long userId;
    private Long friendId;
    private FriendshipStatus status;
    private LocalDateTime createdAt;
}
