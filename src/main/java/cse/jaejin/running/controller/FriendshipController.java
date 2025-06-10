package cse.jaejin.running.controller;

import cse.jaejin.running.dto.FriendshipRequestDto;
import cse.jaejin.running.dto.FriendshipResponseDto;
import cse.jaejin.running.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request")
    public ResponseEntity<FriendshipResponseDto> sendRequest(@RequestBody FriendshipRequestDto dto) {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(dto));
    }

    @PostMapping("/accept/{id}")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long id) {
        friendshipService.acceptFriendRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<FriendshipResponseDto>> getFriends(@PathVariable Long userId) {
        return ResponseEntity.ok(friendshipService.getFriendList(userId));
    }
}
