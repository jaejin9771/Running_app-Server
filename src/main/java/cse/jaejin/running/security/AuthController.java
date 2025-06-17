package cse.jaejin.running.security;

import cse.jaejin.running.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    /** 로그인: access + refresh 토큰 발급 */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        // DB에 refresh token 저장
        refreshTokenService.updateRefreshToken(user.getUsername(), refreshToken);

        LoginResponseDto.UserDto userDto = LoginResponseDto.fromEntity(user);
        return ResponseEntity.ok(new LoginResponseDto(true, "로그인 성공", userDto, accessToken, refreshToken));
    }

    /** access 토큰 재발급 */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (!jwtUtil.validateRefreshToken(refreshToken)) {
            return ResponseEntity.badRequest().body("유효하지 않은 리프레시 토큰입니다.");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        if (!refreshTokenService.isRefreshTokenValid(username, refreshToken)) {
            return ResponseEntity.status(401).body("저장된 리프레시 토큰이 아닙니다.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    /** 로그아웃: DB의 refresh 토큰 제거 */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> request) {
        String username = request.get("username");

        refreshTokenService.removeRefreshToken(username);

        return ResponseEntity.ok("로그아웃 성공: 리프레시 토큰 삭제됨");
    }
}
