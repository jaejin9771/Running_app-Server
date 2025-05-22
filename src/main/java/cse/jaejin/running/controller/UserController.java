package cse.jaejin.running.controller;

import cse.jaejin.running.domain.User;
import cse.jaejin.running.dto.LoginRequest;
import cse.jaejin.running.dto.LoginResponse;
import cse.jaejin.running.dto.UserRequest;
import cse.jaejin.running.dto.UserResponse;
import cse.jaejin.running.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 가입
    @PostMapping
    public ResponseEntity<Long> registerUser(@RequestBody UserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setPhone(request.getPhone());
        user.setBirthDate(request.getBirthDate());
        user.setRole(request.getRole());

        Long id = userService.register(user);
        return ResponseEntity.ok(id);
    }

    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.findUsers();
        List<UserResponse> response = users.stream().map(user -> {
            UserResponse dto = new UserResponse();
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            // 서비스 레이어에서 로그인 검증
            User user = userService.login(request.getUsername(), request.getPassword());

            // 로그인 성공 시 사용자 정보 DTO로 변환 후 반환
            LoginResponse.UserDto dto = LoginResponse.fromEntity(user);
            return ResponseEntity.ok(new LoginResponse(true, "로그인 성공", dto));

        } catch (IllegalArgumentException e) {
            // 로그인 실패 시 예외 메시지 포함하여 401 Unauthorized 반환
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@RequestParam Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(UserResponse.fromEntity(user));
    }


}
