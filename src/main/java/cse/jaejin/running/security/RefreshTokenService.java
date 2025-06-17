package cse.jaejin.running.security;

import cse.jaejin.running.user.User;
import cse.jaejin.running.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserRepository userRepository;

    // 로그인 시 리프레시 토큰 저장/갱신
    public void updateRefreshToken(String username, String refreshToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    // 리프레시 토큰 유효성 확인 (DB와 비교)
    public boolean isRefreshTokenValid(String username, String refreshToken) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return refreshToken.equals(user.getRefreshToken());
    }

    // 로그아웃 시 리프레시 토큰 제거
    public void removeRefreshToken(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRefreshToken(null);
        userRepository.save(user);
    }
}