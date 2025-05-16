package cse.jaejin.running.repository;

import cse.jaejin.running.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // username 기준으로 사용자 조회
    Optional<User> findByUsername(String username);
}
