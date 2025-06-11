package cse.jaejin.running.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @author jaejin
     * @param user 회원 객체
     * @return id
     */
    @Transactional
    public Long register(User user) {
        validateDuplicateUser(user);
        
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateUser(User user) {
        boolean exists = userRepository.findByUsername(user.getUsername()).isPresent();
        if (exists) {
            throw new IllegalStateException("이미 존재하는 유저입니다.");
        }
    }

    public List<User> findUsers() {
        return userRepository.findAll();
    }

        public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<UserResponseDto> searchUsersByKeyword(String keyword) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(keyword);
        return users.stream()
                .map(UserResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
