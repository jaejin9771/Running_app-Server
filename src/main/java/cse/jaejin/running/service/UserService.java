package cse.jaejin.running.service;

import cse.jaejin.running.domain.User;
import cse.jaejin.running.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     * @author jaejin
     * @param user 회원 객체
     * @return id
     */
    @Transactional
    public Long register(User user) {
        validateDuplicateUser(user);
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

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
