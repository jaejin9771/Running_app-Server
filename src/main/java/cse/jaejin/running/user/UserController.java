package cse.jaejin.running.user;

import cse.jaejin.running.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 가입
    @PostMapping
    public ResponseEntity<RegisterResponseDto> registerUser(@RequestBody UserRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
        user.setRole(request.getRole());

        userService.register(user);
        return ResponseEntity.ok(new RegisterResponseDto(true, "회원가입 성공"));
    }

    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<User> users = userService.findUsers();
        List<UserResponseDto> response = users.stream().map(user -> {
            UserResponseDto dto = new UserResponseDto();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setName(user.getName());
            dto.setAge(user.getAge());
            dto.setPhone(user.getPhone());
            dto.setBirthDate(user.getBirthDate());
            dto.setRole(user.getRole());
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public LoginResponseDto.UserDto getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser(); // JWT에서 인증된 사용자 정보
        return LoginResponseDto.fromEntity(user);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(@RequestParam String keyword) {
        List<UserResponseDto> users = userService.searchUsersByKeyword(keyword);
        return ResponseEntity.ok(users);
    }

}
