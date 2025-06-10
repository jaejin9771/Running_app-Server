package cse.jaejin.running.friendship;

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

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteFriend(@RequestParam Long userId, @RequestParam Long friendId) {
        friendshipService.deleteFriendship(userId, friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<Void> rejectFriendRequest(@PathVariable Long id) {
        friendshipService.rejectFriendRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sent/{userId}")
    public ResponseEntity<List<FriendshipResponseDto>> getSentRequests(@PathVariable Long userId) {
        List<FriendshipResponseDto> sent = friendshipService.getSentFriendRequests(userId);
        return ResponseEntity.ok(sent);
    }

    @GetMapping("/received/{userId}")
    public ResponseEntity<List<FriendshipResponseDto>> getReceivedRequests(@PathVariable Long userId) {
        List<FriendshipResponseDto> received = friendshipService.getReceivedFriendRequests(userId);
        return ResponseEntity.ok(received);
    }
}
