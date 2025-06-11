package cse.jaejin.running.security;

import cse.jaejin.running.user.LoginRequestDto;
import cse.jaejin.running.user.LoginResponseDto;
import cse.jaejin.running.user.User;
import cse.jaejin.running.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        LoginResponseDto.UserDto userDto = LoginResponseDto.fromEntity(user);

        return ResponseEntity.ok(new LoginResponseDto(true, "로그인 성공", userDto, token));
    }

}
