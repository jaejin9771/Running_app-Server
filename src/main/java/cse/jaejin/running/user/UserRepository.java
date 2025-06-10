package cse.jaejin.running.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 기준으로 사용자 조회
    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String keyword);
}
